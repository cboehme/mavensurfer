package de.dnb.tools.svnfairy;

import java.util.Objects;

public final class Type {

    private final String type;

    private Type(String type) {
        this.type = type;
    }

    public static Type of(String type) {
        if (type == null) {
            return null;
        }
        return new Type(type);
    }

    @Override
    public boolean equals(Object obj) {
        return Util.equals(this, obj, (a, b) ->
                Objects.equals(a.type, b.type));
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }

    @Override
    public String toString() {
        return type;
    }

}
