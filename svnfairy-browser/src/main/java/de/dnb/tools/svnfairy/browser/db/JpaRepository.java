package de.dnb.tools.svnfairy.browser.db;

import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import de.dnb.tools.svnfairy.browser.model.ArtifactId;
import de.dnb.tools.svnfairy.browser.model.Dependency;
import de.dnb.tools.svnfairy.browser.model.GroupId;
import de.dnb.tools.svnfairy.browser.model.Packaging;
import de.dnb.tools.svnfairy.browser.model.Parent;
import de.dnb.tools.svnfairy.browser.model.Project;
import de.dnb.tools.svnfairy.browser.model.Version;

@ApplicationScoped
public class JpaRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Project getByGav(GroupId groupId,
                            ArtifactId artifactId,
                            Version version) {

        ProjectBean projectBean = findProjectBeanByGav(groupId, artifactId,
                version);
        return mapBeanToProject(projectBean);
    }

    private ProjectBean findProjectBeanByGav(GroupId groupId,
                                             ArtifactId artifactId,
                                             Version version) {

        TypedQuery<ProjectBean> findByGav = entityManager.createNamedQuery(
                "Project.findByGav", ProjectBean.class);
        findByGav.setParameter("groupId", groupId.toString());
        findByGav.setParameter("artifactId", artifactId.toString());
        findByGav.setParameter("version", version.toString());
        return findByGav.getSingleResult();
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
    public Collection<Project> getDependentProjects(Project project) {

        Collection<ProjectBean> projectBeans = findDependentProjectBeans(
                project.getGroupId(), project.getArtifactId());
        return projectBeans.stream()
                .map(this::mapBeanToProject)
                .collect(toList());
    }

    private Collection<ProjectBean> findDependentProjectBeans(GroupId groupId,
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
                    ArtifactId.of(projectBean.artifactId),
                    Version.of(projectBean.parentVersionRange));
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

        DependencyBean dependencyBean = new DependencyBean();
        dependencyBean.groupId = dependency.getGroupId().toString();
        dependencyBean.artifactId = dependency.getArtifactId().toString();
        if (dependency.getVersion() != null) {
            dependencyBean.version = dependency.getVersion().toString();
        }
        return dependencyBean;
    }
}
