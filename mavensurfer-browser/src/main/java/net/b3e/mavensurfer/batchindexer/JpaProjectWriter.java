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

import java.io.Serializable;
import java.util.List;

import javax.batch.api.chunk.ItemWriter;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.b3e.mavensurfer.browser.db.JpaRepository;
import net.b3e.mavensurfer.browser.model.Project;

@Named
public class JpaProjectWriter implements ItemWriter {

    private static final Logger log = LoggerFactory.getLogger("indexing-service");

    private JpaRepository jpaRepository;

    public JpaProjectWriter() {

        // Required by CDI
    }

    @Inject
    public JpaProjectWriter(JpaRepository jpaRepository){

        this.jpaRepository = jpaRepository;
    }

    @Override
    public void open(Serializable checkpoint) throws Exception {

        // Nothing to do
    }

    @Override
    public void close() throws Exception {

        // Nothing to do
    }

    @Override
    public void writeItems(List<Object> items) throws Exception {

        for (Object item : items) {
            if (!(item instanceof Project)) {
                log.warn("Item is not an instance of {}", Project.class.getName());
                log.debug("Item is {}", item);
                continue;
            }
            final Project project = (Project) item;
            log.info("Adding project {} to datastore", project);
            jpaRepository.create(project);
        }
    }

    @Override
    public Serializable checkpointInfo() throws Exception {

        return null;
    }

}
