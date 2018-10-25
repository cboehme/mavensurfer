package de.dnb.tools.svnfairy;

import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.dnb.tools.svnfairy.gateway.PomFileProcessor;
import de.dnb.tools.svnfairy.model.PomFile;
import de.dnb.tools.svnfairy.repositories.pomfile.LocalMavenRepository;
import de.dnb.tools.svnfairy.repositories.pomfile.PomFileRepository;

public class ImporterMain {

    private static final Logger log = LoggerFactory.getLogger(
            ImporterMain.class);

    private final PomFileRepository pomFileRepository;
    private final PomFileProcessor pomFileProcessor;

    private ImporterMain() {

        pomFileRepository = new LocalMavenRepository(
                Paths.get("/home/christoph/Code/svnfairy/svnfairy-importer/testdata"));
        pomFileProcessor = new PomFileProcessor();
    }

    public static void main(String[] args) {

        new ImporterMain().run();
    }

    private void run() {

        for (PomFile pomFile : pomFileRepository.getPoms()) {
            log.info("Send POM to processing engine: {}", pomFile);
            pomFileProcessor.sendToProcessingEngine(pomFile);
        }

    }

}
