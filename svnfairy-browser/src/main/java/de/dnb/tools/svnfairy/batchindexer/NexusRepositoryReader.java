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
package de.dnb.tools.svnfairy.batchindexer;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayDeque;
import java.util.Properties;
import java.util.Queue;

import javax.batch.api.chunk.ItemReader;
import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.dnb.tools.svnfairy.batchindexer.nexusapi.Content;
import de.dnb.tools.svnfairy.batchindexer.nexusapi.ContentItem;
import de.dnb.tools.svnfairy.batchindexer.nexusapi.NexusBrowser;
import de.dnb.tools.svnfairy.browser.model.PomFile;

@Named
public class NexusRepositoryReader implements ItemReader {

    private static final Logger log = LoggerFactory.getLogger("indexing-service");

    private JobContext context;

    private URI nexusUrl;
    private String repositoryId;
    private Queue<String> foldersToProcess;
    private Queue<String> filesToProcess;

    private NexusBrowser nexusBrowser;

    @Inject
    public NexusRepositoryReader(JobContext context) {

        this.context = context;
    }

    public NexusRepositoryReader() {

        // Default constructor required by CDI
    }

    @Override
    public void open(Serializable checkpoint) throws Exception {

        if (checkpoint != null) {
            initFromCheckpoint((Checkpoint) checkpoint);
        } else {
            initFromProperties(getJobParameters());
        }
        createNexusBrowser();
    }

    private void initFromCheckpoint(Checkpoint checkpoint) {

        nexusUrl = checkpoint.getNexusUrl();
        repositoryId = checkpoint.getRepositoryId();
        foldersToProcess = new ArrayDeque<>(checkpoint.getFoldersToProcess());
        filesToProcess = new ArrayDeque<>(checkpoint.getFilesToProcess());

        log.info("({}, {}) Resuming batch from checkpoint. Next folder is {}",
                nexusUrl, repositoryId, foldersToProcess.peek());
    }

    private void initFromProperties(Properties jobProperties)
            throws URISyntaxException {

        nexusUrl = new URI(jobProperties.getProperty("nexus-url"));
        repositoryId = jobProperties.getProperty("repository-id");
        foldersToProcess = new ArrayDeque<>();
        foldersToProcess.add(jobProperties.getProperty("base-path"));
        filesToProcess = new ArrayDeque<>();

        log.info("({}, {}) Starting new batch. Next folder is {}",
                nexusUrl, repositoryId, foldersToProcess.peek());
    }

    private Properties getJobParameters() {

        final JobOperator jobOperator = BatchRuntime.getJobOperator();
        final Properties parameters = jobOperator.getParameters(context.getExecutionId());
        log.info("Job parameters: {}", parameters);
        return parameters;
    }

    private void createNexusBrowser() {

        final Client client = ClientBuilder.newClient();
        final WebTarget target = client.target(nexusUrl);
        nexusBrowser = ((ResteasyWebTarget) target).proxy(NexusBrowser.class);
    }

    @Override
    public void close() throws Exception {

        nexusUrl = null;
        repositoryId = null;
        foldersToProcess = null;
        filesToProcess = null;
        nexusBrowser = null;
    }

    @Override
    public Object readItem() throws Exception {

        loadSomeFilesToProcess();
        if (filesToProcess.isEmpty()) {
            log.info("({}, {}) No more files to process",
                    nexusUrl, repositoryId);
            return null;
        }
        final String nextFileToProcess = filesToProcess.remove();
        return loadFile(nextFileToProcess);
    }

    private void loadSomeFilesToProcess() {
        while (filesToProcess.isEmpty() && !foldersToProcess.isEmpty()) {
            final String nextFolderToLoad = foldersToProcess.remove();
            loadFolder(nextFolderToLoad);
        }
    }

    private void loadFolder(String path) {

        log.info("({}, {}) Loading folder {}",
                nexusUrl, repositoryId, path);

        final Content content = nexusBrowser.browse(
                repositoryId, stripLeadingSlash(path));

        for (ContentItem item : content.getData()) {
            if (item.isLeaf()) {
                if (item.getText().endsWith(".pom")) {
                    filesToProcess.add(item.getRelativePath());
                }
            } else {
                foldersToProcess.add(item.getRelativePath());
            }
        }
    }

    private String stripLeadingSlash(String path) {

        if (path.startsWith("/")) {
            return path.substring(1);
        }
        return path;
    }

    private PomFile loadFile(String path) {

        log.info("({}, {}) Loading POM {}",
                nexusUrl, repositoryId, path);
        final byte[] contents = nexusBrowser.getContent(repositoryId, path);
        return new PomFile(path, contents);
    }

    @Override
    public Serializable checkpointInfo() throws Exception {

        return new Checkpoint(nexusUrl, repositoryId, filesToProcess,
                foldersToProcess);
    }


    private static final class Checkpoint implements Serializable {

        private final URI nexusUrl;
        private final String repositoryId;
        private final Queue<String> foldersToProcess;
        private final Queue<String> filesToProcess;

        Checkpoint(URI nexusUrl,
                   String repositoryId,
                   Queue<String> foldersToProcess,
                   Queue<String> filesToProcess) {

            this.nexusUrl = nexusUrl;
            this.repositoryId = repositoryId;
            this.foldersToProcess = new ArrayDeque<>(foldersToProcess);
            this.filesToProcess = new ArrayDeque<>(filesToProcess);
        }


        URI getNexusUrl() {
            return nexusUrl;
        }

        String getRepositoryId() {
            return repositoryId;
        }

        Queue<String> getFoldersToProcess() {
            return foldersToProcess;
        }

        Queue<String> getFilesToProcess() {
            return filesToProcess;
        }

    }

}
