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

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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
import org.apache.maven.model.building.ModelSource2;
import org.apache.maven.model.resolution.ModelResolver;
import org.apache.maven.project.ProjectBuildingRequest;
import org.apache.maven.project.ProjectModelResolver;
import org.apache.maven.repository.internal.MavenRepositorySystemUtils;
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

    @Inject
    public ProcessPomFile(JpaRepository jpaRepository) {

        this.jpaRepository = jpaRepository;
    }

    public ProcessPomFile() {

        // Required by CDI
    }

    public void processPomFile(PomFile pomFile) {

        final Project project = makeEffectivePom(pomFile);
        if (project != null) {
            jpaRepository.create(project);
        }
    }

    private Project makeEffectivePom(PomFile pomFile) {

        final ModelSource2 modelSource = new ByteArrayModelSource(
                pomFile.getName(), pomFile.getContents());

        final ModelBuildingRequest request = new DefaultModelBuildingRequest();
        request.setModelSource(modelSource);
        request.setValidationLevel(VALIDATION_LEVEL_MINIMAL);
        request.setProcessPlugins(false);
        request.setTwoPhaseBuilding(true);
        request.setSystemProperties(System.getProperties());
        request.setModelResolver(createModelResolver());

        final ModelBuilder modelBuilder = new DefaultModelBuilderFactory().newInstance();

        ModelBuildingResult result;
        final Model rawModel;
        final Model effectiveModel;
        try {
            result = modelBuilder.build(request);
            rawModel = result.getEffectiveModel().clone();
            result = modelBuilder.build(request, result);
            effectiveModel = result.getEffectiveModel();

        } catch (ModelBuildingException e) {
            log.error("Could not build effective POM", e);
            return null;
        }

        return mapModelToProject(pomFile.getName(), effectiveModel, rawModel);
    }

    private ModelResolver createModelResolver() {
        DefaultServiceLocator serviceLocator = MavenRepositorySystemUtils.newServiceLocator();
        serviceLocator.addService(RepositoryConnectorFactory.class, BasicRepositoryConnectorFactory.class);
        serviceLocator.addService(TransporterFactory.class, HttpTransporterFactory.class);

        RepositorySystem repositorySystem = serviceLocator.getService(RepositorySystem.class);

        DefaultRepositorySystemSession session = MavenRepositorySystemUtils.newSession();

        LocalRepository localRepository = new LocalRepository("/home/christoph/.m2/repository");
        LocalRepositoryManager localRepositoryManager =
                repositorySystem.newLocalRepositoryManager(session, localRepository);
        session.setLocalRepositoryManager(localRepositoryManager);
        session.setReadOnly();

        serviceLocator.getService(RepositoryConnectorFactory.class);
        RemoteRepository remoteRepository = new RemoteRepository.Builder(
                "maven-central", "default", "http://repo1.maven.org/maven2/").build();
        List<RemoteRepository> remoteRepositories =
                repositorySystem.newResolutionRepositories(session, singletonList(remoteRepository));
        RemoteRepositoryManager remoteRepositoryManager =
                serviceLocator.getService(RemoteRepositoryManager.class);

        return new ProjectModelResolver(session, new RequestTrace(null),
                repositorySystem, remoteRepositoryManager, remoteRepositories,
                ProjectBuildingRequest.RepositoryMerging.POM_DOMINANT, null);
    }

    private Project mapModelToProject(String sourceName,
                                      Model model,
                                      Model rawModel) {

        final Project project = new Project(sourceName);

        project.setGroupId(GroupId.of(model.getGroupId()));
        project.setArtifactId(ArtifactId.of(model.getArtifactId()));
        project.setVersion(Version.of(model.getVersion()));
        project.setPackaging(Packaging.of(model.getPackaging()));

        if (model.getParent() != null) {
            final GroupId parentGroupId = GroupId.of(
                    model.getParent().getGroupId());
            final ArtifactId parentArtifactId = ArtifactId.of(
                    model.getParent().getArtifactId());
            final VersionRequirement parentVersion = VersionRequirement.of(
                    model.getParent().getVersion());
            final Parent parent = Parent.of(parentGroupId, parentArtifactId,
                    parentVersion);
            project.setParent(parent);
        }

        final Set<DependencyId> dependencies = new HashSet<>();
        for (Dependency dependency : rawModel.getDependencies()) {
            dependencies.add(DependencyId.of(dependency));
        }
        log.info("Dependencies: {}", dependencies);

        for (Dependency dependency : model.getDependencies()) {
            final DependencyId dependencyId = DependencyId.of(dependency);
            if (!dependencies.contains(dependencyId)) {
                log.info("Skipping dependency: {}", dependency);
                continue;
            }
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

        if (rawModel.getDependencyManagement() != null) {
            log.info("Process dependency management");
            for (Dependency dependency : rawModel.getDependencyManagement().getDependencies()) {
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

    private static final class DependencyId {

        final String groupId;
        final String artifactId;
        final String classifier;
        final String type;

        private DependencyId(Dependency dependency) {

            this.groupId = dependency.getGroupId();
            this.artifactId = dependency.getArtifactId();
            this.classifier = dependency.getClassifier();
            this.type = dependency.getType();
        }

        static DependencyId of(Dependency dependency) {
            return new DependencyId(dependency);
        }

        @Override
        public boolean equals(Object obj) {
            return Util.equals(this, obj, (a, b) ->
                Objects.equals(a.groupId, b.groupId) &&
                Objects.equals(a.artifactId, b.artifactId) &&
                Objects.equals(a.classifier, b.classifier) &&
                Objects.equals(a.type, b.type));
        }

        @Override
        public int hashCode() {
            return Objects.hash(groupId, artifactId, classifier, type);
        }

    }

}
