package de.dnb.tools.svnfairy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import de.dnb.tools.svnfairy.collectors.PomCollector;
import de.dnb.tools.svnfairy.collectors.PomFile;
import de.dnb.tools.svnfairy.collectors.SubversionCollector;

/**
 * Searches the repository for Maven POMs and outputs group id, artifact id and version for each project found.
 */
public class FindAllProjects {

    private static final String repositoryBaseUrl = "svn://svn.dnb.de:23690/dnb";

    private static final Logger log = LoggerFactory.getLogger(FindAllProjects.class);

    private final PomParser pomParser;

    public FindAllProjects() {
        pomParser = new PomParser();
    }

    public static void main(String[] args)
            throws XPathExpressionException, IOException, SAXException, ParserConfigurationException {

        new FindAllProjects().run();
    }

    private void run()
            throws SAXException, ParserConfigurationException, XPathExpressionException, IOException {
        Collection<Project> projects = new ArrayList<>();
        PomCollector pomCollector = new SubversionCollector(repositoryBaseUrl, "");
        for (PomFile pomFile : pomCollector.getPoms()) {
            log.info("Processing pom: " + pomFile.getName());
            Project project = pomParser.parsePom(pomFile);
            projects.add(project);
        }

        try (
                FileWriter projectsList = new FileWriter("projects.txt");
        ) {
            for (Project project : projects) {
                projectsList.append(project.getFile());
                projectsList.append(", ");
                projectsList.append(String.valueOf(project.getGroupId()));
                projectsList.append(", ");
                projectsList.append(String.valueOf(project.getArtifactId()));
                projectsList.append(", ");
                projectsList.append(String.valueOf(project.getPackaging()));
                projectsList.append('\n');
            }
        } catch (IOException e) {
            log.error("Cannot write projects list: " + e.getMessage());
        }
    }

}
