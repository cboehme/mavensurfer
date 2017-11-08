package de.dnb.tools.svnfairy;

public class Dependency {

    private GroupId groupId;
    private ArtifactId artifactId;
    private VersionRequirement version;
    private Classifier classifier;
    private Type type;
    private Scope scope;
    private boolean optional;

    public GroupId getGroupId() {
        return groupId;
    }

    public void setGroupId(GroupId groupId) {
        this.groupId = groupId;
    }

    public ArtifactId getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(ArtifactId artifactId) {
        this.artifactId = artifactId;
    }

    public VersionRequirement getVersion() {
        return version;
    }

    public void setVersion(VersionRequirement version) {
        this.version = version;
    }

    public Classifier getClassifier() {
        return classifier;
    }

    public void setClassifier(Classifier classifier) {
        this.classifier = classifier;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public boolean isOptional() {
        return optional;
    }

    public void setOptional(boolean optional) {
        this.optional = optional;
    }

}
