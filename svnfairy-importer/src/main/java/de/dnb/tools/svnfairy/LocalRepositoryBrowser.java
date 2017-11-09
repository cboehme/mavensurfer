package de.dnb.tools.svnfairy;

import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.dnb.tools.svnfairy.repositories.pomfile.LocalMavenRepository;
import de.dnb.tools.svnfairy.repositories.pomfile.PomFileRepository;

public class LocalRepositoryBrowser {

    private static final Logger log = LoggerFactory.getLogger(
            LocalRepositoryBrowser.class);

    public static void main(String[] args) {
        new LocalRepositoryBrowser().run();
    }

    private void run() {
        PomFileRepository pomFileRepository = new LocalMavenRepository(
                Paths.get("/home/christoph/.m2/repository"));
        pomFileRepository.getPoms().forEach(System.out::println);
    }

}
