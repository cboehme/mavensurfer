package de.dnb.tools.svnfairy;

import java.util.Objects;

public final class Version {

    private final String version;

    private Version(String version) {
        this.version = version;
    }

    public static Version of(String version) {
        if (version == null) {
            return null;
        }
        return new Version(version);
    }

    @Override
    public boolean equals(Object obj) {
        return Util.equals(this, obj, (a, b) ->
                Objects.equals(a.version, b.version));
    }

    @Override
    public int hashCode() {
        return Objects.hash(version);
    }

    @Override
    public String toString() {
        return version;
    }

}
