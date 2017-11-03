package de.dnb.tools.svnfairy;

import java.util.Objects;

public final class GroupId {

    private final String groupId;

    private GroupId(String groupId) {
        this.groupId = groupId;
    }

    public static GroupId of(String groupId) {
        if (groupId == null) {
            return null;
        }
        return new GroupId(groupId);
    }

    @Override
    public boolean equals(Object obj) {
        return Util.equals(this, obj, (a, b) ->
                Objects.equals(a.groupId, b.groupId));
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId);
    }

    @Override
    public String toString() {
        return groupId;
    }

}
