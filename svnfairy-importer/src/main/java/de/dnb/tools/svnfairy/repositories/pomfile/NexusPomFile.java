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

import de.dnb.tools.svnfairy.model.PomFile;
import de.dnb.tools.svnfairy.repositories.pomfile.nexusapi.NexusBrowser;

public class NexusPomFile implements PomFile {

    private final NexusBrowser nexusBrowser;
    private final String repositoryId;
    private final String path;

    public NexusPomFile(NexusBrowser nexusBrowser,
                        String repositoryId,
                        String path) {
        this.nexusBrowser = nexusBrowser;
        this.repositoryId = repositoryId;
        this.path = path;
    }

    @Override
    public String getName() {
        return path;
    }

    @Override
    public byte[] getContents() {
        return nexusBrowser.getContent(repositoryId, path);
    }

}
