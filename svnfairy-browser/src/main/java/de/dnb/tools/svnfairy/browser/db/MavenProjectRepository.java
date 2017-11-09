package de.dnb.tools.svnfairy.browser.db;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import de.dnb.tools.svnfairy.model.ArtifactId;
import de.dnb.tools.svnfairy.model.GroupId;
import de.dnb.tools.svnfairy.model.Packaging;
import de.dnb.tools.svnfairy.model.Project;
import de.dnb.tools.svnfairy.model.Version;

@ApplicationScoped
public class MavenProjectRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Project getByGav(GroupId groupId,
                            ArtifactId artifactId,
                            Version version) {

        PomBean pomBean = findPomBeanByGav(groupId, artifactId, version);
        return mapPomBeanToMavenProject(pomBean);
    }

    private PomBean findPomBeanByGav(GroupId groupId,
                                     ArtifactId artifactId,
                                     Version version) {

        TypedQuery<PomBean> findByGav = entityManager.createNamedQuery(
                "Pom.findByGav", PomBean.class);
        findByGav.setParameter("groupId", groupId.toString());
        findByGav.setParameter("artifactId", artifactId.toString());
        findByGav.setParameter("version", version.toString());
        return findByGav.getSingleResult();
    }

    private Project mapPomBeanToMavenProject(PomBean pomBean) {

        Project project = new Project(pomBean.file);
        project.setGroupId(GroupId.of(pomBean.groupId));
        project.setArtifactId(ArtifactId.of(pomBean.artifactId));
        project.setVersion(Version.of(pomBean.version));
        project.setPackaging(Packaging.of(pomBean.packaging));
        return project;
    }

    @Transactional
    public void create(Project project) {
        PomBean pomBean = mapMavenProjectToPomBean(project);
        entityManager.persist(pomBean);
    }

    private PomBean mapMavenProjectToPomBean(Project project) {

        PomBean pomBean = new PomBean();
        pomBean.file = project.getFile();
        pomBean.groupId = project.getGroupId().toString();
        pomBean.artifactId = project.getArtifactId().toString();
        pomBean.version = project.getVersion().toString();
        pomBean.packaging = project.getPackaging().toString();
        return pomBean;
    }

}
