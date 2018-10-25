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
package net.b3e.mavensurfer.browser.model;

import static java.util.Objects.hash;
import static java.util.Objects.requireNonNull;

import java.util.Arrays;
import java.util.Objects;

import net.b3e.mavensurfer.browser.Util;

public class PomFile {

    private final String name;
    private final byte[] contents;


    public PomFile(String name, byte[] contents) {
        requireNonNull(name);
        requireNonNull(contents);

        this.name = name;
        this.contents = Arrays.copyOf(contents, contents.length);
    }

    public String getName() {
        return name;
    }

    public byte[] getContents() {
        return Arrays.copyOf(contents, contents.length);
    }

    @Override
    public boolean equals(Object obj) {
        return Util.equals(this, obj, (a, b) ->
                Objects.equals(a.name, b.name));
    }

    @Override
    public int hashCode() {
        return hash(name);
    }

    @Override
    public String toString() {
        return name;
    }

}
