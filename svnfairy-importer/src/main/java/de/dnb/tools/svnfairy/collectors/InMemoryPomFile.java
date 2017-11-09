package de.dnb.tools.svnfairy.collectors;

import java.util.Objects;

import de.dnb.tools.svnfairy.Util;
import de.dnb.tools.svnfairy.model.PomFile;

public class InMemoryPomFile implements PomFile {

    private final String name;
    private final byte[] contents;

    public InMemoryPomFile(String name, byte[] contents) {
        this.name = name;
        this.contents = contents;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public byte[] getContents() {
        return contents;
    }

    @Override
    public boolean equals(Object obj) {
        return Util.equals(this, obj, (a, b) ->
                Objects.equals(a.name, b.name));
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return name;
    }

}
