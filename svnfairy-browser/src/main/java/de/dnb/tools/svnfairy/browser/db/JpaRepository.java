package de.dnb.tools.svnfairy.browser.db;

import static java.util.stream.Collectors.toList;

import java.util.Collection;

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

    private Project mapBeanToProject(ProjectBean projectBean) {

        Project project = new Project(projectBean.file);
        project.setGroupId(GroupId.of(projectBean.groupId));
        project.setArtifactId(ArtifactId.of(projectBean.artifactId));
        project.setVersion(Version.of(projectBean.version));
        project.setPackaging(Packaging.of(projectBean.packaging));
        return project;
    }

    @Transactional
    public Collection<Project> getDependentProjects(Project project) {

        Collection<ProjectBean> projectBeans = findDependentProjectBeans(
                project.getGroupId().get(), project.getArtifactId());
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
    public void create(Project project) {

        ProjectBean projectBean = mapProjectToBean(project);
        entityManager.persist(projectBean);

        for (Dependency dependency : project.getDependencies()) {
            DependencyBean dependencyBean = mapDependencyToBean(dependency);
            dependencyBean.owner = projectBean;
            entityManager.persist(dependencyBean);
        }
    }

    private ProjectBean mapProjectToBean(Project project) {

        ProjectBean projectBean = new ProjectBean();
        projectBean.file = project.getFile();
        projectBean.groupId = project.getGroupId().get().toString();
        projectBean.artifactId = project.getArtifactId().toString();
        projectBean.version = project.getVersion().get().toString();
        if (project.getPackaging() != null) {
            projectBean.packaging = project.getPackaging().toString();
        }
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
