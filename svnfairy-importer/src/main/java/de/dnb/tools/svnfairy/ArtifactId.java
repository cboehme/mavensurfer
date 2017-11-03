package de.dnb.tools.svnfairy;

import java.util.Objects;

public final class ArtifactId {

    private final String artifactId;

    private ArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public static ArtifactId of(String artifactId) {
        if (artifactId == null) {
            return null;
        }
        return new ArtifactId(artifactId);
    }

    @Override
    public boolean equals(Object obj) {
        return Util.equals(this, obj, (a, b) ->
                Objects.equals(a.artifactId, b.artifactId));
    }

    @Override
    public int hashCode() {
        return Objects.hash(artifactId);
    }

    @Override
    public String toString() {
        return artifactId;
    }

}
