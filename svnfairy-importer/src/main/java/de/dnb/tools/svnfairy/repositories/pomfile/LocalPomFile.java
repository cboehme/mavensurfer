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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import de.dnb.tools.svnfairy.Util;
import de.dnb.tools.svnfairy.model.PomFile;

final class LocalPomFile implements PomFile {

    private final Path path;

    LocalPomFile(Path path) {
        this.path = path;
    }

    @Override
    public String getName() {
        return path.toString();
    }

    @Override
    public byte[] getContents() {
        try (
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ) {
            Files.copy(path, byteStream);
            return byteStream.toByteArray();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public boolean equals(Object obj) {
        return Util.equals(this, obj, (a, b) ->
                Objects.equals(a.path, b.path));
    }

    @Override
    public int hashCode() {
        return Objects.hash(path);
    }

    @Override
    public String toString() {
        return path.toString();
    }

}
