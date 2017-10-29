package de.dnb.tools.svnfairy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

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
        Collection<MavenProject> mavenProjects = new ArrayList<>();
        PomCollector pomCollector = new SubversionCollector(repositoryBaseUrl, "");
        for (PomFile pom : pomCollector.getPoms()) {
            log.info("Processing pom: " + pom.getName());
            MavenProject project = pomParser.parsePom(pom.getName(), pom.getContents());
            mavenProjects.add(project);
        }

        try (
                FileWriter projectsList = new FileWriter("projects.txt");
        ) {
            for (MavenProject project : mavenProjects) {
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
