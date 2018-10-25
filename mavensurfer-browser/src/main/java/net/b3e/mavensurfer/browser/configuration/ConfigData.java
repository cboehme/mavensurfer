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
package net.b3e.mavensurfer.browser.configuration;

import static java.util.Objects.requireNonNull;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import net.b3e.mavensurfer.browser.Util;

public class ConfigData {

    private final String name;
    private final ZonedDateTime lastModified;
    private final byte[] data;

    public ConfigData(String name, byte[] data) {

        this(name, null, data);
    }

    public ConfigData(String name, ZonedDateTime lastModified, byte[] data) {

        requireNonNull(name);
        requireNonNull(data);

        this.name = name;
        this.lastModified = lastModified;
        this.data = Arrays.copyOf(data, data.length);
    }

    public String getName() {

        return name;
    }

    public Optional<ZonedDateTime> getLastModified() {

        return Optional.ofNullable(lastModified);
    }

    public byte[] getData() {

        return Arrays.copyOf(data, data.length);
    }

    @Override
    public boolean equals(Object obj) {

        return Util.equals(this, obj, (a, b) ->
                Objects.equals(a.name, b.name) &&
                Objects.equals(a.lastModified, b.lastModified));
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, lastModified);
    }

    @Override
    public String toString() {

        return String.format("ConfigData [%s, %s]", name, lastModified);
    }

}
