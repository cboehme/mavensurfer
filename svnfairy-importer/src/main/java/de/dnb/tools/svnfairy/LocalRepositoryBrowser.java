package de.dnb.tools.svnfairy;

import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.dnb.tools.svnfairy.collectors.LocalRepositoryCollector;
import de.dnb.tools.svnfairy.collectors.PomCollector;

public class LocalRepositoryBrowser {

    private static final Logger log = LoggerFactory.getLogger(
            LocalRepositoryBrowser.class);

    public static void main(String[] args) {
        new LocalRepositoryBrowser().run();
    }

    private void run() {
        PomCollector pomCollector = new LocalRepositoryCollector(
                Paths.get("/home/christoph/.m2/repository"));
        pomCollector.getPoms().forEach(System.out::println);
    }

}
