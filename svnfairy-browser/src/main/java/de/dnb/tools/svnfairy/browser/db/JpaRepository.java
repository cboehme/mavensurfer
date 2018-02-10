package de.dnb.tools.svnfairy.browser.db;

import static java.util.stream.Collectors.maxBy;
import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import de.dnb.tools.svnfairy.browser.model.ArtifactId;
import de.dnb.tools.svnfairy.browser.model.Classifier;
import de.dnb.tools.svnfairy.browser.model.Dependency;
import de.dnb.tools.svnfairy.browser.model.GroupId;
import de.dnb.tools.svnfairy.browser.model.Packaging;
import de.dnb.tools.svnfairy.browser.model.Parent;
import de.dnb.tools.svnfairy.browser.model.Project;
import de.dnb.tools.svnfairy.browser.model.Scope;
import de.dnb.tools.svnfairy.browser.model.Type;
import de.dnb.tools.svnfairy.browser.model.Version;
import de.dnb.tools.svnfairy.browser.model.VersionRequirement;

@ApplicationScoped
public class JpaRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Optional<Project> getByGav(GroupId groupId,
                                      ArtifactId artifactId,
                                      Version version) {

        return findProjectBeanByGav(groupId, artifactId, version)
                .map(this::mapBeanToProject);
    }

    private Optional<ProjectBean> findProjectBeanByGav(GroupId groupId,
                                                       ArtifactId artifactId,
                                                       Version version) {

        TypedQuery<ProjectBean> findByGav = entityManager.createNamedQuery(
                "Project.findByGav", ProjectBean.class);
        findByGav.setParameter("groupId", groupId.toString());
        findByGav.setParameter("artifactId", artifactId.toString());
        findByGav.setParameter("version", version.toString());
        try {
            return Optional.of(findByGav.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Transactional
    public List<Project> findProjectsWith(GroupId groupId,
                                          ArtifactId artifactId) {

        final TypedQuery<ProjectBean> find = entityManager.createNamedQuery(
                "Project.findByGroupIdAndArtifactId", ProjectBean.class);
        find.setParameter("groupId", groupId.toString());
        find.setParameter("artifactId", artifactId.toString());
        return find.getResultList().stream()
                .map(this::mapBeanToProject)
                .collect(toList());
    }

    @Transactional
    public List<GroupId> findGroupIds() {

        final TypedQuery<String> find = entityManager.createNamedQuery(
                "Project.findGroupIds", String.class);
        return find.getResultList().stream()
                .map(GroupId::of)
                .collect(toList());
    }

    @Transactional
    public List<ArtifactId> findArtifactIdsIn(GroupId groupId) {

        final TypedQuery<String> find = entityManager.createNamedQuery(
                "Project.findArtifactIds", String.class);
        find.setParameter("groupId", groupId.toString());
        return find.getResultList().stream()
                .map(ArtifactId::of)
                .collect(toList());
    }

    @Transactional
    public Collection<Dependency> getDependenciesOf(Project project) {

        final ProjectBean projectBean = findProjectBean(project);

        return findDependencyBeans(projectBean).stream()
                .map(this::mapBeanToDependency)
                .collect(toList());
    }

    private ProjectBean findProjectBean(Project project) {

        return findProjectBeanByGav(project.getGroupId(),
                project.getArtifactId(), project.getVersion()).orElse(null);
    }

    private Collection<DependencyBean> findDependencyBeans(ProjectBean projectBean) {

        final TypedQuery<DependencyBean> findDependencies =
                entityManager.createNamedQuery("Dependency.findByOwner",
                        DependencyBean.class);
        findDependencies.setParameter("owner", projectBean);
        return findDependencies.getResultList();
    }

    @Transactional
    public Collection<Project> getDependentProjects(Project project) {

        Collection<ProjectWithDependency> projectWithDeps = findDependentProjectBeans(
                project.getGroupId(), project.getArtifactId());
        return projectWithDeps.stream()
                .map(projectWithDep -> {
                    Project p = mapBeanToProject(projectWithDep.project);
                    p.addDependency(mapBeanToDependency(projectWithDep.dependency));
                    return p;
                })
                .collect(toList());
    }

    private Collection<ProjectWithDependency> findDependentProjectBeans(GroupId groupId,
                                                              ArtifactId artifactId) {

        Query findByDependency = entityManager.createNamedQuery(
                "Project.findByDependency");
        findByDependency.setParameter("groupId", groupId.toString());
        findByDependency.setParameter("artifactId", artifactId.toString());
        return findByDependency.getResultList();
    }

    @Transactional
    public Collection<Project> getChildProjectsOf(Project project) {

        Collection<ProjectBean> projectBeans = findChildProjectBeans(
                project.getGroupId(), project.getArtifactId());
        return projectBeans.stream()
                .map(this::mapBeanToProject)
                .collect(toList());
    }

    private Collection<ProjectBean> findChildProjectBeans(GroupId groupId,
                                                          ArtifactId artifactId) {

        Query findByParent = entityManager.createNamedQuery(
                "Project.findByParent");
        findByParent.setParameter("parentGroupId", groupId.toString());
        findByParent.setParameter("parentArtifactId", artifactId.toString());
        return findByParent.getResultList();

    }

    @Transactional
    public void create(Project project) {

        ProjectBean projectBean = mapProjectToBean(project);
        entityManager.persist(projectBean);

        for (Dependency dependency : project.getDependencies()) {
            DependencyBean dependencyBean = mapDependencyToBean(dependency);
            dependencyBean.owner = projectBean;
            entityManager.persist(dependencyBean);
        }
    }

    private Project mapBeanToProject(ProjectBean projectBean) {

        Project project = new Project(projectBean.file);
        project.setGroupId(GroupId.of(projectBean.groupId));
        project.setArtifactId(ArtifactId.of(projectBean.artifactId));
        project.setVersion(Version.of(projectBean.version));
        project.setPackaging(Packaging.of(projectBean.packaging));
        if (projectBean.parentGroupId != null
                && projectBean.parentArtifactId != null) {
            Parent parent = Parent.of(
                    GroupId.of(projectBean.parentGroupId),
                    ArtifactId.of(projectBean.parentArtifactId),
                    VersionRequirement.of(projectBean.parentVersionRange));
            project.setParent(parent);
        }
        return project;
    }

    private ProjectBean mapProjectToBean(Project project) {

        ProjectBean projectBean = new ProjectBean();
        projectBean.file = project.getFile();
        projectBean.groupId = project.getGroupId().toString();
        projectBean.artifactId = project.getArtifactId().toString();
        projectBean.version = project.getVersion().toString();
        if (project.getPackaging() != null) {
            projectBean.packaging = project.getPackaging().toString();
        }
        project.getParent()
                .ifPresent(projectBean::setParentCoordinates);
        return projectBean;
    }

    private DependencyBean mapDependencyToBean(Dependency dependency) {

        final DependencyBean dependencyBean = new DependencyBean();
        dependencyBean.groupId = dependency.getGroupId().toString();
        dependencyBean.artifactId = dependency.getArtifactId().toString();
        if (dependency.getVersion() != null) {
            dependencyBean.version = dependency.getVersion().toString();
        }
        dependencyBean.classifier = dependency.getClassifier().toString();
        dependencyBean.type = dependency.getType().toString();
        dependencyBean.scope = dependency.getScope().name();
        dependencyBean.optional = dependency.isOptional();
        return dependencyBean;
    }

    private Dependency mapBeanToDependency(DependencyBean dependencyBean) {
        final Dependency dependency = new Dependency();
        dependency.setGroupId(GroupId.of(dependencyBean.groupId));
        dependency.setArtifactId(ArtifactId.of(dependencyBean.artifactId));
        dependency.setVersion(VersionRequirement.of(dependencyBean.version));
        dependency.setClassifier(Classifier.of(dependencyBean.classifier));
        dependency.setType(Type.of(dependencyBean.type));
        dependency.setScope(Scope.valueOf(dependencyBean.scope));
        dependency.setOptional(dependencyBean.optional);
        return dependency;
    }
}
