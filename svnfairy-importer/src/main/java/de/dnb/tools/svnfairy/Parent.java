package de.dnb.tools.svnfairy;

import java.util.Objects;

public final class Parent {

    private final GroupId groupId;
    private final ArtifactId artifactId;
    private final Version version;

    private Parent(GroupId groupId,
                   ArtifactId artifactId,
                   Version version) {

        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
    }

    public static Parent of(GroupId groupdId,
                            ArtifactId artifactId,
                            Version version) {

        return new Parent(groupdId, artifactId, version);
    }

    public GroupId getGroupId() {
        return groupId;
    }

    public ArtifactId getArtifactId() {
        return artifactId;
    }

    public Version getVersion() {
        return version;
    }

    @Override
    public boolean equals(Object obj) {
        return Util.equals(this, obj, (a, b) ->
                Objects.equals(a.groupId, b.groupId)
                && Objects.equals(a.artifactId, b.artifactId)
                && Objects.equals(a.version, b.version));
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, artifactId, version);
    }

    @Override
    public String toString() {
        return groupId + ":" + artifactId + ":" + version;
    }

}
