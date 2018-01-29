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
package de.dnb.tools.svnfairy.api.impl;

import static java.util.Objects.requireNonNull;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.dnb.tools.svnfairy.api.ProjectsResource;
import de.dnb.tools.svnfairy.api.datatypes.JsonArtifactId;
import de.dnb.tools.svnfairy.api.datatypes.JsonCollection;
import de.dnb.tools.svnfairy.api.datatypes.JsonGroupId;
import de.dnb.tools.svnfairy.api.datatypes.JsonVersion;
import de.dnb.tools.svnfairy.api.datatypes.Pom;
import de.dnb.tools.svnfairy.browser.ProcessPomFile;
import de.dnb.tools.svnfairy.browser.QueryProjects;
import de.dnb.tools.svnfairy.browser.model.ArtifactId;
import de.dnb.tools.svnfairy.browser.model.GroupId;
import de.dnb.tools.svnfairy.browser.model.PomFile;
import de.dnb.tools.svnfairy.browser.model.Version;

@RequestScoped
public class ProjectsResourceImpl implements ProjectsResource {

    private static final Logger log = LoggerFactory.getLogger(
            ProjectsResourceImpl.class);

    @Inject
    private ProcessPomFile processPomFile;
    @Inject
    private QueryProjects queryProjects;

    @Inject
    private JsonMapper map;

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
    public Response listGroupIds() {

        final List<GroupId> groupIds = queryProjects.getGroupIds();
        final JsonCollection<JsonGroupId> jsonGroupIds = map.toJson(groupIds);
        return Response.ok(jsonGroupIds).build();
    }

    @Override
    public Response listArtifactIdsFor(String groupIdString) {

        requireNonNull(groupIdString);

        final GroupId groupId = GroupId.of(groupIdString);
        final List<ArtifactId> artifactIds = queryProjects.getArtifactIdsIn(groupId);
        if (artifactIds.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        final JsonCollection<JsonArtifactId> jsonArtifactIds =
                map.toJson(groupId, artifactIds);
        return Response.ok(jsonArtifactIds).build();
    }

    @Override
    public Response listVersionsFor(String groupIdString,
                                    String artifactIdString) {

        requireNonNull(groupIdString);
        requireNonNull(artifactIdString);

        final GroupId groupId = GroupId.of(groupIdString);
        final ArtifactId artifactId = ArtifactId.of(artifactIdString);
        final List<Version> versions = queryProjects.getVersionsOf(groupId, artifactId);
        if (versions.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        final JsonCollection<JsonVersion> jsonVersions =
                map.toJson(groupId, artifactId, versions);
        return Response.ok(jsonVersions).build();
    }

}
