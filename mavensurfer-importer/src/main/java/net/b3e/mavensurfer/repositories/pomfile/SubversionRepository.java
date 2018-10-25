/*
 * Copyright 2018 Christoph Böhme
 *
 * Licensed under the Apache License, Version 2.0 the "License";
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.b3e.mavensurfer.repositories.pomfile;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

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

import net.b3e.mavensurfer.model.PomFile;

public class SubversionRepository implements PomFileRepository {

    private static final Logger log = LoggerFactory.getLogger(
            SubversionRepository.class);

    private static final Set<String> ignoredDirectories =
            Stream.of("tags", "branches", "src", "_deleted").collect(toSet());

    static {
        SVNRepositoryFactoryImpl.setup();
    }

    private final SVNRepository repository;
    private final String path;

    public SubversionRepository(String repositoryUrl, String path) {
        this.path = path;
        try {
            SVNURL repositorySvnUrl = SVNURL.parseURIEncoded(repositoryUrl);
            ISVNAuthenticationManager authenticationManager =
                    SVNWCUtil.createDefaultAuthenticationManager();
            repository = SVNRepositoryFactory.create(repositorySvnUrl);
            repository.setAuthenticationManager(authenticationManager);
        } catch (SVNException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterable<PomFile> getPoms() {
        try {
            return collectPoms(path).stream()
                    .map(path -> new SubversionPomFile(repository, path))
                    .collect(toList());
        } catch (SVNException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isRepositoryWalkable() throws SVNException {
        log.info("Repository Root: {}", repository.getRepositoryRoot(true));
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

    private Collection<String> collectPoms(String path) throws SVNException {
        List<String> poms = new ArrayList<>();
        for (SVNDirEntry entry : getDirEntries(path)) {
            String entryPath = path + (path.isEmpty() ? "" : "/") + entry.getName();
            if (entry.getKind() == SVNNodeKind.FILE) {
                if ("pom.xml".equals(entry.getName())) {
                    log.info("Found POM: {}", entryPath);
                    poms.add(entryPath);
                }
            } else if (entry.getKind() == SVNNodeKind.DIR) {
                if (!ignoredDirectories.contains(entry.getName())) {
                    log.debug("Processing directory: {}", entryPath);
                    poms.addAll(collectPoms(entryPath));
                }
            } else {
                log.error("Unexpected entry type ({}): {}", entry.getKind(),
                        entryPath);
            }
        }
        return poms;
    }

    @SuppressWarnings("unchecked")
    private Collection<SVNDirEntry> getDirEntries(String path)
            throws SVNException {
        return (Collection<SVNDirEntry>) repository.getDir(
                path, -1, null, (Collection) null);
    }

}
