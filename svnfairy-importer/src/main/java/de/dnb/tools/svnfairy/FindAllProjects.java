package de.dnb.tools.svnfairy;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import de.dnb.tools.svnfairy.model.PomFile;
import de.dnb.tools.svnfairy.model.Project;
import de.dnb.tools.svnfairy.repositories.pomfile.PomFileRepository;
import de.dnb.tools.svnfairy.repositories.pomfile.SubversionRepository;

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
        PomFileRepository pomFileRepository = new SubversionRepository(repositoryBaseUrl, "");
        for (PomFile pomFile : pomFileRepository.getPoms()) {
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