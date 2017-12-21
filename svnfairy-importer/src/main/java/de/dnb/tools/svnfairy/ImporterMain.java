package de.dnb.tools.svnfairy;

import java.io.IOException;

import javax.ws.rs.core.UriBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import de.dnb.tools.svnfairy.gateway.PomFileProcessor;
import de.dnb.tools.svnfairy.model.PomFile;
import de.dnb.tools.svnfairy.repositories.pomfile.NexusRepository;
import de.dnb.tools.svnfairy.repositories.pomfile.PomFileRepository;

public class ImporterMain {

    private static final Logger log = LoggerFactory.getLogger(
            ImporterMain.class);

    private final PomFileRepository pomFileRepository;
    private final PomFileProcessor pomFileProcessor;

    private ImporterMain() {

//        pomFileRepository = new LocalMavenRepository(
//                Paths.get("/home/christoph/Code/svnfairy/svnfairy-importer/testdata"));
/*
        pomFileRepository = new NexusRepository(
                UriBuilder.fromUri("http://nexus.dnb.de/").build(),
                "releases", "/");
*/
        pomFileRepository = new NexusRepository(
                UriBuilder.fromUri("https://oss.sonatype.org/").build(),
                "releases", "/org/culturegraph/");
        pomFileProcessor = new PomFileProcessor();
    }

    public static void main(String[] args) throws SAXException,
            ParserConfigurationException, XPathExpressionException, IOException {

        new ImporterMain().run();
    }

    private void run() throws SAXException, ParserConfigurationException,
            XPathExpressionException, IOException {

        for (PomFile pomFile : pomFileRepository.getPoms()) {
            log.info("Send POM to processing engine: {}", pomFile);
            pomFileProcessor.sendToProcessingEngine(pomFile);
        }

    }

}
