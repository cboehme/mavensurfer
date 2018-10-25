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
package net.b3e.mavensurfer.browser.maven;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.maven.building.Source;

public class InMemorySource implements Source {

    private final String name;
    private final byte[] contents;

    InMemorySource(String name, byte[] contents) {

        this.name = name;
        this.contents = contents;
    }

    @Override
    public InputStream getInputStream() throws IOException {

        return new ByteArrayInputStream(contents);
    }

    @Override
    public String getLocation() {

        return name;
    }

}
