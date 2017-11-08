package de.dnb.tools.svnfairy.collectors;

public interface PomCollector {

    Iterable<PomFile> getPoms();

}
