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

import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

import de.dnb.tools.svnfairy.model.PomFile;

public class LocalMavenRepository implements PomFileRepository {

    private final Path repositoryDir;

    public LocalMavenRepository(Path repositoryDir) {
        this.repositoryDir = repositoryDir;
    }

    @Override
    public Iterable<PomFile> getPoms() {
        try {
            return Files.walk(repositoryDir)
                    .filter(path -> path.toFile().isFile())
                    .filter(path -> path.toString().endsWith(".pom"))
                    .map(LocalPomFile::new)
                    .collect(toList());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
