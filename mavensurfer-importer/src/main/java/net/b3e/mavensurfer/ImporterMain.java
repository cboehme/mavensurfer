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
package net.b3e.mavensurfer;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.b3e.mavensurfer.gateway.PomFileProcessor;
import net.b3e.mavensurfer.model.PomFile;
import net.b3e.mavensurfer.repositories.pomfile.LocalMavenRepository;
import net.b3e.mavensurfer.repositories.pomfile.PomFileRepository;

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
