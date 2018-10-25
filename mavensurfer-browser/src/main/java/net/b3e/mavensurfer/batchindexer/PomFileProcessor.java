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
package net.b3e.mavensurfer.batchindexer;

import javax.batch.api.chunk.ItemProcessor;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.b3e.mavensurfer.browser.maven.ExtractInformation;
import net.b3e.mavensurfer.browser.model.PomFile;
import net.b3e.mavensurfer.browser.model.Project;

@Named
public class PomFileProcessor implements ItemProcessor {

    private static final Logger log = LoggerFactory.getLogger("indexing-service");

    private ExtractInformation extractInformation;

    public PomFileProcessor() {

        // Required by CDI
    }

    @Inject
    public PomFileProcessor(ExtractInformation extractInformation) {

        this.extractInformation = extractInformation;
    }

    @Override
    public Project processItem(Object item) throws Exception {

        if (!(item instanceof PomFile)) {
            log.warn("Item is not an instance of {}", PomFile.class.getName());
            log.debug("Item is {}", item);
            return null;
        }
        final PomFile pomFile = (PomFile) item;
        log.info("Processing {}", pomFile.getName());
        return extractInformation.fromPom(pomFile);
    }

}
