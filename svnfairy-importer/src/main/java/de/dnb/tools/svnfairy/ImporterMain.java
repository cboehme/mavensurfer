package de.dnb.tools.svnfairy;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import de.dnb.tools.svnfairy.model.PomFile;
import de.dnb.tools.svnfairy.model.Project;
import de.dnb.tools.svnfairy.repositories.pomfile.LocalMavenRepository;
import de.dnb.tools.svnfairy.repositories.pomfile.PomFileRepository;

public class ImporterMain {

    private static final Logger log = LoggerFactory.getLogger(
            ImporterMain.class);

    private PomParser pomParser = new PomParser();
    private ParentResolvingService parentResolvingService =
            new ParentResolvingService();

    public static void main(String[] args) throws SAXException,
            ParserConfigurationException, XPathExpressionException, IOException {

        new ImporterMain().run();
    }

    private void run() throws SAXException, ParserConfigurationException,
            XPathExpressionException, IOException {

        PomFileRepository pomFileRepository = new LocalMavenRepository(
                Paths.get("/home/christoph/.m2/repository"));

        Collection<Project> projects = new ArrayList<>();

        for (PomFile pomFile : pomFileRepository.getPoms()) {
            log.debug("Parsing POM: {}", pomFile);
            Project project = pomParser.parsePom(pomFile);
            if (project != null) {
                projects.add(project);
            }
        }

        parentResolvingService.resolveProjects(projects);

        projects.removeIf(Project::hasIncompleteCoordinates);

        projects.forEach(System.out::println);
    }

}
