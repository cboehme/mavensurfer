/*
 * Copyright 2017 Christoph Böhme
 *
 * Licensed under the Apache License, Version 2.0 the "License";
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.dnb.tools.svnfairy.browser;

import static java.util.Collections.singletonList;
import static org.apache.maven.model.building.ModelBuildingRequest.VALIDATION_LEVEL_MINIMAL;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.building.DefaultModelBuilderFactory;
import org.apache.maven.model.building.DefaultModelBuildingRequest;
import org.apache.maven.model.building.ModelBuilder;
import org.apache.maven.model.building.ModelBuildingException;
import org.apache.maven.model.building.ModelBuildingRequest;
import org.apache.maven.model.building.ModelBuildingResult;
import org.apache.maven.model.building.ModelSource;
import org.apache.maven.model.building.ModelSource2;
import org.apache.maven.model.resolution.ModelResolver;
import org.apache.maven.model.resolution.UnresolvableModelException;
import org.apache.maven.project.ProjectBuildingRequest;
import org.apache.maven.project.ProjectModelResolver;
import org.apache.maven.repository.internal.MavenRepositorySystemUtils;
import org.apache.maven.settings.Mirror;
import org.apache.maven.settings.Proxy;
import org.apache.maven.settings.Settings;
import org.apache.maven.settings.building.DefaultSettingsBuilderFactory;
import org.apache.maven.settings.building.DefaultSettingsBuildingRequest;
import org.apache.maven.settings.building.SettingsBuilder;
import org.apache.maven.settings.building.SettingsBuildingException;
import org.apache.maven.settings.building.SettingsBuildingRequest;
import org.apache.maven.settings.building.SettingsBuildingResult;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RequestTrace;
import org.eclipse.aether.connector.basic.BasicRepositoryConnectorFactory;
import org.eclipse.aether.impl.DefaultServiceLocator;
import org.eclipse.aether.impl.RemoteRepositoryManager;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.repository.LocalRepositoryManager;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.spi.connector.RepositoryConnectorFactory;
import org.eclipse.aether.spi.connector.transport.TransporterFactory;
import org.eclipse.aether.transport.http.HttpTransporterFactory;
import org.eclipse.aether.util.repository.DefaultMirrorSelector;
import org.eclipse.aether.util.repository.DefaultProxySelector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.dnb.tools.svnfairy.browser.db.JpaRepository;
import de.dnb.tools.svnfairy.browser.model.ArtifactId;
import de.dnb.tools.svnfairy.browser.model.Classifier;
import de.dnb.tools.svnfairy.browser.model.GroupId;
import de.dnb.tools.svnfairy.browser.model.Packaging;
import de.dnb.tools.svnfairy.browser.model.Parent;
import de.dnb.tools.svnfairy.browser.model.PomFile;
import de.dnb.tools.svnfairy.browser.model.Project;
import de.dnb.tools.svnfairy.browser.model.Scope;
import de.dnb.tools.svnfairy.browser.model.Type;
import de.dnb.tools.svnfairy.browser.model.Version;
import de.dnb.tools.svnfairy.browser.model.VersionRequirement;

@RequestScoped
public class ProcessPomFile {

    private static final Logger log = LoggerFactory.getLogger(
            ProcessPomFile.class);

    private JpaRepository jpaRepository;
    private Configuration configuration;

    private Settings settings;
    private ModelResolver modelResolver;

    @Inject
    public ProcessPomFile(JpaRepository jpaRepository,
                          Configuration configuration) {

        this.jpaRepository = jpaRepository;
        this.configuration = configuration;
    }

    @PostConstruct
    public void init() {
        settings = readSettings();
        modelResolver = createModelResolver(settings);
    }

    public ProcessPomFile() {

        // Required by CDI
    }

    public void processPomFile(PomFile pomFile) {

        final ModelSource modelSource = new ByteArrayModelSource(
                pomFile.getName(), pomFile.getContents());
        final Project project = makeEffectivePom(modelSource);
        if (project != null) {
            jpaRepository.create(project);
        }
    }

    public void process(GroupId groupId,
                        ArtifactId artifactId,
                        Version version) {

        try {
            final ModelSource modelSource = modelResolver.resolveModel(
                    groupId.toString(), artifactId.toString(), version.toString());
            final Project project = makeEffectivePom(modelSource);
            if (project != null) {
                jpaRepository.create(project);
            }
        } catch (UnresolvableModelException e) {
            log.error("Failed to retrieve resource");
        }
    }

    private Project makeEffectivePom(ModelSource modelSource) {

        final ModelBuildingRequest request = createModelBuildingRequest(
                modelSource, modelResolver);
        final ModelBuilder modelBuilder =
                new DefaultModelBuilderFactory().newInstance();

        final Model partlyProcessedModel;
        final Model fullyProcessedModel;
        try {
            ModelBuildingResult result = modelBuilder.build(request);
            partlyProcessedModel = result.getEffectiveModel().clone();
            result = modelBuilder.build(request, result);
            fullyProcessedModel = result.getEffectiveModel();

        } catch (ModelBuildingException e) {
            log.error("Could not build effective POM", e);
            return null;
        }

        return mapModelToProject(modelSource.getLocation(), partlyProcessedModel,
                fullyProcessedModel);
    }

    private ModelBuildingRequest createModelBuildingRequest(ModelSource modelSource,
                                                            ModelResolver modelResolver) {

        return new DefaultModelBuildingRequest()
                .setModelSource(modelSource)
                .setValidationLevel(VALIDATION_LEVEL_MINIMAL)
                .setProcessPlugins(false)
                .setTwoPhaseBuilding(true)
                .setSystemProperties(System.getProperties())
                .setModelResolver(modelResolver);
    }

    private ModelResolver createModelResolver(Settings settings) {

        final DefaultServiceLocator serviceLocator = MavenRepositorySystemUtils.newServiceLocator();
        serviceLocator.addService(RepositoryConnectorFactory.class, BasicRepositoryConnectorFactory.class);
        serviceLocator.addService(TransporterFactory.class, HttpTransporterFactory.class);
        serviceLocator.getService(RepositoryConnectorFactory.class);

        final RepositorySystem repositorySystem = serviceLocator.getService(RepositorySystem.class);

        final DefaultRepositorySystemSession session = MavenRepositorySystemUtils.newSession();
        final DefaultMirrorSelector mirrorSelector = new DefaultMirrorSelector();
        for (Mirror mirror : settings.getMirrors()) {
            mirrorSelector.add(mirror.getId(), mirror.getUrl(), mirror.getLayout(),
                    false, mirror.getMirrorOf(), mirror.getMirrorOfLayouts());
        }
        session.setMirrorSelector(mirrorSelector);
        final DefaultProxySelector proxySelector = new DefaultProxySelector();
        for (Proxy proxy : settings.getProxies()) {
            // TODO: Support proxies with authentication
            // TODO: Decrypt passwords in settings
            proxySelector.add(new org.eclipse.aether.repository.Proxy(
                    proxy.getProtocol(), proxy.getHost(), proxy.getPort()),
                    proxy.getNonProxyHosts());
        }
        String localRepositoryDirectory = settings.getLocalRepository();
        if (localRepositoryDirectory == null) {
            localRepositoryDirectory = configuration.getDefaultLocalRepository().toString();
        }
        final LocalRepository localRepository = new LocalRepository(localRepositoryDirectory);
        final LocalRepositoryManager localRepositoryManager =
                repositorySystem.newLocalRepositoryManager(session, localRepository);
        session.setLocalRepositoryManager(localRepositoryManager);
        session.setReadOnly();

        // TODO: Read remoterepos from settings
        final RemoteRepository remoteRepository = new RemoteRepository.Builder(
                "maven-central", "default", "http://repo1.maven.org/maven2/").build();
        final List<RemoteRepository> remoteRepositories =
                repositorySystem.newResolutionRepositories(session, singletonList(remoteRepository));
        final RemoteRepositoryManager remoteRepositoryManager =
                serviceLocator.getService(RemoteRepositoryManager.class);

        return new ProjectModelResolver(session, new RequestTrace(null),
                repositorySystem, remoteRepositoryManager, remoteRepositories,
                ProjectBuildingRequest.RepositoryMerging.POM_DOMINANT, null);
    }

    private Settings readSettings() {

        final SettingsBuildingRequest request = createSettingsBuildingRequest();
        final SettingsBuilder builder = new DefaultSettingsBuilderFactory().newInstance();

        final Settings settings;
        try {
            final SettingsBuildingResult result = builder.build(request);
            settings = result.getEffectiveSettings();
        } catch (SettingsBuildingException e) {
            log.error("Could not read Maven settings: {}", e);
            return new Settings();
        }
        return settings;
    }

    private SettingsBuildingRequest createSettingsBuildingRequest() {

        return new DefaultSettingsBuildingRequest()
                .setGlobalSettingsFile(configuration.getGlobalMavenSettings().toFile())
                .setUserSettingsFile(configuration.getUserMavenSettings().toFile())
                .setSystemProperties(System.getProperties());
    }

    private Project mapModelToProject(String sourceName,
                                      Model partlyProcessedModel,
                                      Model fullyProcessedModel) {

        final Project project = new Project(sourceName);

        project.setGroupId(GroupId.of(fullyProcessedModel.getGroupId()));
        project.setArtifactId(ArtifactId.of(fullyProcessedModel.getArtifactId()));
        project.setVersion(Version.of(fullyProcessedModel.getVersion()));
        project.setPackaging(Packaging.of(fullyProcessedModel.getPackaging()));

        if (fullyProcessedModel.getParent() != null) {
            final GroupId parentGroupId = GroupId.of(
                    fullyProcessedModel.getParent().getGroupId());
            final ArtifactId parentArtifactId = ArtifactId.of(
                    fullyProcessedModel.getParent().getArtifactId());
            final VersionRequirement parentVersion = VersionRequirement.of(
                    fullyProcessedModel.getParent().getVersion());
            final Parent parent = Parent.of(parentGroupId, parentArtifactId,
                    parentVersion);
            project.setParent(parent);
        }

        for (Dependency dependency : fullyProcessedModel.getDependencies()) {
            final de.dnb.tools.svnfairy.browser.model.Dependency dependencyRef =
                    new de.dnb.tools.svnfairy.browser.model.Dependency();
            dependencyRef.setGroupId(GroupId.of(dependency.getGroupId()));
            dependencyRef.setArtifactId(ArtifactId.of(dependency.getArtifactId()));
            dependencyRef.setVersion(VersionRequirement.of(dependency.getVersion()));
            dependencyRef.setClassifier(Classifier.of(dependency.getClassifier()));
            dependencyRef.setType(Type.of(dependency.getType()));
            dependencyRef.setScope(Scope.valueOf(dependency.getScope().toUpperCase()));
            dependencyRef.setOptional(dependency.isOptional());
            project.addDependency(dependencyRef);
        }

        if (partlyProcessedModel.getDependencyManagement() != null) {
            log.info("Process dependency management");
            for (Dependency dependency : partlyProcessedModel.getDependencyManagement().getDependencies()) {
                log.info("processing dependency management: {}", dependency);
                if ("import".equals(dependency.getScope()) && "pom".equals(dependency.getType())) {
                    final de.dnb.tools.svnfairy.browser.model.Dependency dependencyRef =
                            new de.dnb.tools.svnfairy.browser.model.Dependency();
                    dependencyRef.setGroupId(GroupId.of(dependency.getGroupId()));
                    dependencyRef.setArtifactId(ArtifactId.of(dependency.getArtifactId()));
                    dependencyRef.setVersion(VersionRequirement.of(dependency.getVersion()));
                    dependencyRef.setClassifier(Classifier.of(dependency.getClassifier()));
                    dependencyRef.setType(Type.of(dependency.getType()));
                    dependencyRef.setScope(Scope.valueOf(dependency.getScope().toUpperCase()));
                    dependencyRef.setOptional(dependency.isOptional());
                    project.addDependency(dependencyRef);
                }
            }
        }

        return project;
    }

}
