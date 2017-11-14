package de.dnb.tools.svnfairy.browser.db;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import de.dnb.tools.svnfairy.browser.model.ArtifactId;
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
    public void create(Project project) {
        ProjectBean projectBean = mapProjectToBean(project);
        entityManager.persist(projectBean);
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
        return projectBean;
    }

}
