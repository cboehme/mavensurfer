package de.dnb.tools.svnfairy;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

final public class Gav {

    private final GroupId groupId;
    private final ArtifactId artifactId;
    private final Version version;

    private Gav(GroupId groupId, ArtifactId artifactId, Version version) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
    }

    public static Gav of(GroupId groupId,
                         ArtifactId artifactId,
                         Version version) {
        requireNonNull(groupId);
        requireNonNull(artifactId);
        requireNonNull(version);

        return new Gav(groupId, artifactId, version);
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
                Objects.equals(a.groupId, b.groupId) &&
                Objects.equals(a.artifactId, b.artifactId) &&
                Objects.equals(a.version, b.version));
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
