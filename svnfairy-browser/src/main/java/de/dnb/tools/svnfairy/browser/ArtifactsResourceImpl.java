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
package de.dnb.tools.svnfairy.browser;

import static java.util.Objects.requireNonNull;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

import de.dnb.tools.svnfairy.api.ArtifactsResource;
import de.dnb.tools.svnfairy.browser.model.ArtifactId;
import de.dnb.tools.svnfairy.browser.model.GroupId;
import de.dnb.tools.svnfairy.browser.model.Version;

@RequestScoped
public class ArtifactsResourceImpl implements ArtifactsResource {

    @Inject
    private ProcessPomFile  processPomFile;

    @Override
    public Response putArtifact(String coordinates) {

        requireNonNull(coordinates);

        final String[] gav = coordinates.split(":");
        final GroupId groupId = GroupId.of(gav[0]);
        final ArtifactId artifactId = ArtifactId.of(gav[1]);
        final Version version = Version.of(gav[2]);

        processPomFile.process(groupId, artifactId, version);

        return Response.ok().build();
    }

}
