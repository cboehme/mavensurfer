package de.dnb.tools.svnfairy;

public class MavenCoordinates {

    private final String groupId;
    private final String artifactId;
    private final String version;
    private final String classifier;
    private final String packaging;

    private MavenCoordinates(String groupId,
                             String artifactId,
                             String version,
                             String classifier,
                             String packaging) {

        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.classifier = classifier;
        this.packaging = packaging;
    }

    public static Builder withArtifactId(String artifactId) {
        return new Builder(artifactId);
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

    public String getClassifier() {
        return classifier;
    }

    public String getPackaging() {
        return packaging;
    }

    public static final class Builder {

        private final String artifactId;

        private String groupId = "";
        private String version = "";
        private String classifier = "";
        private String packaging = "";

        private Builder(String artifactId) {
            this.artifactId = artifactId;
        }

        public Builder withGroupId(String groupId) {
            this.groupId = groupId;
            return this;
        }

        public Builder withVersion(String version) {
            this.version = version;
            return this;
        }

        public Builder withClassifier(String classifier) {
            this.classifier = classifier;
            return this;
        }

        public Builder withPackaging(String packaging) {
            this.packaging = packaging;
            return this;
        }

        public MavenCoordinates create() {
            return new MavenCoordinates(groupId, artifactId, version, classifier, packaging);
        }

    }

}
