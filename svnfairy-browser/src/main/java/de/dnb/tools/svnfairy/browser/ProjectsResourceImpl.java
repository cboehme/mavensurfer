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

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.dnb.tools.svnfairy.api.Pom;
import de.dnb.tools.svnfairy.api.ProjectsResource;
import de.dnb.tools.svnfairy.browser.model.ArtifactId;
import de.dnb.tools.svnfairy.browser.model.GroupId;
import de.dnb.tools.svnfairy.browser.model.PomFile;
import de.dnb.tools.svnfairy.browser.model.Version;

@RequestScoped
public class ProjectsResourceImpl implements ProjectsResource {

    private static final Logger log = LoggerFactory.getLogger(
            ProjectsResourceImpl.class);

    @Inject
    private ProcessPomFile  processPomFile;

    private final Base64.Decoder base64Decoder = Base64.getDecoder();

    @Override
    public Response indexPom(Pom pom) {

        requireNonNull(pom);

        final byte[] contents = base64Decoder.decode(pom.getContents());
        final PomFile pomFile = new PomFile(pom.getName(), contents);

        log.info("Received {}", pomFile.getName());
        log.debug("POM: {}", new String(pomFile.getContents(),
                StandardCharsets.UTF_8));

        processPomFile.processPomFile(pomFile);

        return Response.ok().build();
    }

    @Override
    public Response indexGav(String groupIdString,
                             String artifactIdString,
                             String versionString) {

        requireNonNull(groupIdString);
        requireNonNull(artifactIdString);
        requireNonNull(versionString);

        final GroupId groupId = GroupId.of(groupIdString);
        final ArtifactId artifactId = ArtifactId.of(artifactIdString);
        final Version version = Version.of(versionString);

        processPomFile.process(groupId, artifactId, version);

        return Response.ok().build();
    }

}
