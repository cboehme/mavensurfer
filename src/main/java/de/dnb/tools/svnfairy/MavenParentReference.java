package de.dnb.tools.svnfairy;

public class MavenParentReference {

    private final String groupId;
    private final String artifactId;
    private final String version;
    private final String relativePath;

    private MavenParentReference(String groupId,
                                 String artifactId,
                                 String version,
                                 String relativePath) {

        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.relativePath = relativePath;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public String getVersion() {
        return version;
    }

    public String getRelativePath() {
        return relativePath;
    }


}
