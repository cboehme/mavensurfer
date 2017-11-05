package de.dnb.tools.svnfairy.browser;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named("pom")
@RequestScoped
public class PomBean {

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

    public String getGav() {
        return groupId + ":" + artifactId + ":" + version;
    }

}
