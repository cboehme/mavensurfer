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

    public static Builder build() {
        return new Builder();
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

    public static final class Builder {

        private String groupId;
        private String artifactId;
        private String version;
        private String relativePath;

        private Builder() {}

        public Builder withGroupId(String groupId) {
            this.groupId = groupId;
            return this;
        }

        public Builder withArtifactId(String artifactId) {
            this.artifactId = artifactId;
            return this;
        }

        public Builder withVersion(String version) {
            this.version = version;
            return this;
        }

        public Builder withRelativePath(String relativePath) {
            this.relativePath = relativePath;
            return this;
        }

        public MavenParentReference create() {
            return new MavenParentReference(groupId, artifactId, version,
                relativePath);
        }

    }

}
