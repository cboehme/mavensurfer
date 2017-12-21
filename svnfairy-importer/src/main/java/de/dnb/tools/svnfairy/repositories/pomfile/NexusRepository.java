/*
 * Copyright 2017 Christoph Böhme
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
package de.dnb.tools.svnfairy.repositories.pomfile;

import static java.util.stream.Collectors.toList;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.dnb.tools.svnfairy.model.PomFile;
import de.dnb.tools.svnfairy.repositories.pomfile.nexusapi.Content;
import de.dnb.tools.svnfairy.repositories.pomfile.nexusapi.ContentItem;
import de.dnb.tools.svnfairy.repositories.pomfile.nexusapi.NexusBrowser;

public class NexusRepository implements PomFileRepository {

    private static final Logger log = LoggerFactory.getLogger(
            NexusRepository.class);

    private final NexusBrowser nexusBrowser;
    private final String repositoryId;
    private final String basePath;

    public NexusRepository(URI nexusUrl, String repositoryId, String basePath) {

        final Client client = ClientBuilder.newClient();
        final WebTarget target = client.target(nexusUrl);
        this.nexusBrowser =
                ((ResteasyWebTarget) target).proxy(NexusBrowser.class);

        this.repositoryId = repositoryId;
        this.basePath = basePath;
    }

    @Override
    public Iterable<PomFile> getPoms() {

        return collectPoms(basePath).stream()
                .map(path -> new NexusPomFile(nexusBrowser, repositoryId, path))
                .collect(toList());
    }

    private Collection<String> collectPoms(String path) {

        log.debug("Retrieving contents of: {}", path);
        final Collection<String> poms = new ArrayList<>();
        final Content content = nexusBrowser.browse(repositoryId,
                stripLeadingSlash(path));
        for (ContentItem item : content.getData()) {
            log.debug("Processing item: {}", item);
            if (item.isLeaf()) {
                if (item.getText().endsWith(".pom")) {
                    log.info("Adding pom: {}", item.getRelativePath());
                    poms.add(item.getRelativePath());
                }
            } else {
                poms.addAll(collectPoms(item.getRelativePath()));
            }
        }
        return poms;
    }

    private String stripLeadingSlash(String path) {
        if (path.startsWith("/")) {
            return path.substring(1);
        }
        return path;
    }

}
