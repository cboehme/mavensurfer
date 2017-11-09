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

import java.util.Objects;

import de.dnb.tools.svnfairy.Util;
import de.dnb.tools.svnfairy.model.PomFile;

public class InMemoryPomFile implements PomFile {

    private final String name;
    private final byte[] contents;

    public InMemoryPomFile(String name, byte[] contents) {
        this.name = name;
        this.contents = contents;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public byte[] getContents() {
        return contents;
    }

    @Override
    public boolean equals(Object obj) {
        return Util.equals(this, obj, (a, b) ->
                Objects.equals(a.name, b.name));
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return name;
    }

}
