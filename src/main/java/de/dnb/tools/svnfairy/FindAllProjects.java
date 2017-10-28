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

    private static final String pomFile = "pom.xml";
    private static final String repositoryBaseUrl = "svn://svn.dnb.de:23690/dnb";
    private static final Set<String> ignoredDirectories = Stream.of("tags", "branches", "src", "_deleted").collect(toSet());

    private static final Logger log = LoggerFactory.getLogger(FindAllProjects.class);

    private final Collection<String> poms = new ArrayList<>();
    private final Collection<MavenProject> mavenProjects = new ArrayList<>();

    private SVNRepository repository;
    private PomParser pomParser;

    public FindAllProjects() throws XPathExpressionException {
        pomParser = new PomParser();
    }

    public static void main(String[] args)
            throws SVNException, XPathExpressionException, IOException, SAXException, ParserConfigurationException {

        SVNRepositoryFactoryImpl.setup();
        new FindAllProjects().run();
    }

    private void run()
            throws SVNException, SAXException, ParserConfigurationException, XPathExpressionException, IOException {

        SVNURL repositoryUrl = SVNURL.parseURIEncoded(repositoryBaseUrl);
        ISVNAuthenticationManager authenticationManager = SVNWCUtil.createDefaultAuthenticationManager();

        repository = SVNRepositoryFactory.create(repositoryUrl);
        repository.setAuthenticationManager(authenticationManager);

        log.info("Repository Root: {}", repository.getRepositoryRoot(true));
        log.info("Repository UUID: {}", repository.getRepositoryUUID(true));

        if (isRepositoryWalkable()) {
            collectPoms("");
        }

        for (String pom : poms) {
            log.info("Processing pom: " + pom);
            ByteArrayOutputStream pomStream = new ByteArrayOutputStream();
            downloadFile(pom, pomStream);
            MavenProject project = pomParser.parsePom(pom, pomStream.toByteArray());
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

    private boolean isRepositoryWalkable() throws SVNException {
        SVNNodeKind kind = repository.checkPath("", -1);
        if (kind == SVNNodeKind.NONE) {
            log.error("Repository path does not exist: {}", repository.getLocation());
            return false;
        } else if (kind == SVNNodeKind.FILE) {
            log.error("Repository path must point to a directory: {}", repository.getLocation());
            return false;
        }
        return true;
    }

    private void collectPoms(String path) throws SVNException {
        for (SVNDirEntry entry : getDirEntries(path)) {
            String entryPath = path + (path.isEmpty() ? "" : "/") + entry.getName();
            if (entry.getKind() == SVNNodeKind.FILE) {
                if (pomFile.equals(entry.getName())) {
                    log.info("Found POM file: {}", entryPath);
                    poms.add(entryPath);
                }
            } else if (entry.getKind() == SVNNodeKind.DIR) {
                if (!ignoredDirectories.contains(entry.getName())) {
                    log.debug("Processing directory: {}", entryPath);
                    collectPoms(entryPath);
                }
            } else {
               log.error("Unexpected entry type ({}): {}", entry.getKind(), entryPath);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private Collection<SVNDirEntry> getDirEntries(String path) throws SVNException {
        return (Collection<SVNDirEntry>) repository.getDir(
                path, -1, null, (Collection) null);
    }

    private void downloadFile(String path, OutputStream stream) throws SVNException {
        repository.getFile(path, -1, null, stream);
    }

}
