package de.dnb.tools.svnfairy.collectors;

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

}
