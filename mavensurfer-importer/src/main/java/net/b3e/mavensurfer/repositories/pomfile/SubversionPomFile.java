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
package net.b3e.mavensurfer.repositories.pomfile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Objects;

import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.io.SVNRepository;

import net.b3e.mavensurfer.Util;
import net.b3e.mavensurfer.model.PomFile;

class SubversionPomFile implements PomFile {

    private final SVNRepository repository;
    private final String path;

    SubversionPomFile(SVNRepository repository, String path) {
        this.repository = repository;
        this.path = path;
    }

    @Override
    public String getName() {
        return path;
    }

    @Override
    public byte[] getContents() {
        try (
                ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ) {
            repository.getFile(path, -1, null, byteStream);
            return byteStream.toByteArray();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (SVNException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object obj) {
        return Util.equals(this, obj, (a, b) ->
                Objects.equals(a.repository.getLocation(),
                        b.repository.getLocation()) &&
                Objects.equals(a.path, b.path));
    }

    @Override
    public int hashCode() {
        return Objects.hash(repository.getLocation(), path);
    }

    @Override
    public String toString() {
        String location = repository.getLocation().toString();
        if (!location.endsWith("/")) {
            location += "/";
        }
        return location + path;
    }

}
