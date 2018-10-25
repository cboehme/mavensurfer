package de.dnb.tools.svnfairy;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.dnb.tools.svnfairy.gateway.PomFileProcessor;
import de.dnb.tools.svnfairy.model.PomFile;
import de.dnb.tools.svnfairy.repositories.pomfile.LocalMavenRepository;
import de.dnb.tools.svnfairy.repositories.pomfile.PomFileRepository;

public class ImporterMain {

    private static final Logger LOG = LoggerFactory.getLogger(ImporterMain.class);

    private final PomFileRepository pomFileRepository;
    private final PomFileProcessor pomFileProcessor;

    private ImporterMain(Path repositoryLocation) {

        pomFileRepository = new LocalMavenRepository(repositoryLocation);
        pomFileProcessor = new PomFileProcessor();
    }

    public static void main(String[] args) {

        if (args.length != 1) {
            LOG.error("Program argument missing. " +
                    "Please provide the location of the local Maven repository.");
            return;
        }
        new ImporterMain(Paths.get(args[0])).run();
    }

    private void run() {

        for (PomFile pomFile : pomFileRepository.getPoms()) {
            LOG.info("Send POM to processing engine: {}", pomFile);
            pomFileProcessor.sendToProcessingEngine(pomFile);
        }

    }

}
