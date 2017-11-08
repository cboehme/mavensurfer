package de.dnb.tools.svnfairy.browser;

import java.util.Collections;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import de.dnb.tools.svnfairy.ArtifactId;
import de.dnb.tools.svnfairy.GroupId;
import de.dnb.tools.svnfairy.Project;
import de.dnb.tools.svnfairy.Version;
import de.dnb.tools.svnfairy.browser.db.MavenProjectRepository;

@Named("pom")
@RequestScoped
public class PomBean {

    @Inject
    private MavenProjectRepository repository;

    private String groupId;
    private String artifactId;
    private String version;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getName() {
        return artifactId;
    }

    public String getGav() {
        return groupId + ":" + artifactId + ":" + version;
    }

    public String getFile() {
        Project project = repository.getByGav(GroupId.of(groupId),
                ArtifactId.of(artifactId), Version.of(version));
        return project.getFile();
    }

    public List<PomBean> getDependents() {
        return Collections.emptyList();
    }

}
