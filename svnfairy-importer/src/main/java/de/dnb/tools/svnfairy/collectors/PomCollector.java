package de.dnb.tools.svnfairy.collectors;

import de.dnb.tools.svnfairy.model.PomFile;

public interface PomCollector {

    Iterable<PomFile> getPoms();

}
