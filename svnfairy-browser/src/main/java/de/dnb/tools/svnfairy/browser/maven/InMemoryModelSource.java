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
package de.dnb.tools.svnfairy.browser.maven;

import java.net.URI;

import org.apache.maven.model.building.ModelSource2;

class InMemoryModelSource extends InMemorySource implements ModelSource2 {

    InMemoryModelSource(String name, byte[] contents) {

        super(name, contents);
    }

    @Override
    public ModelSource2 getRelatedSource(String relPath) {

        return null;
    }

    @Override
    public URI getLocationURI() {

        return null;
    }

}