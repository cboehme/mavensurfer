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

import java.net.URI;

import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import de.dnb.tools.svnfairy.api.ProjectResource;
import de.dnb.tools.svnfairy.api.datatypes.JsonProject;
import de.dnb.tools.svnfairy.browser.ProcessPomFile;
import de.dnb.tools.svnfairy.browser.QueryProjects;
import de.dnb.tools.svnfairy.browser.model.ArtifactId;
import de.dnb.tools.svnfairy.browser.model.GroupId;
import de.dnb.tools.svnfairy.browser.model.Version;

public class ProjectResourceImpl implements ProjectResource {

    @Context
    private UriInfo uriInfo;

    @Inject
    private ProcessPomFile processPomFile;
    @Inject
    private QueryProjects queryProjects;

    private final JsonMapper map;

    public ProjectResourceImpl() {

        map = new JsonMapper();
        map.setProjectUri(this::getProjectUri);
    }

    @Override
    public JsonProject getProject(String groupIdString,
                                  String artifactIdString,
                                  String versionString) {

        final GroupId groupId = GroupId.of(groupIdString);
        final ArtifactId artifactId = ArtifactId.of(artifactIdString);
        final Version version = Version.of(versionString);

        return queryProjects.getProject(groupId, artifactId, version)
                .map(map::toJson)
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public Response indexGav(String groupIdString,
                             String artifactIdString,
                             String versionString) {

        final GroupId groupId = GroupId.of(groupIdString);
        final ArtifactId artifactId = ArtifactId.of(artifactIdString);
        final Version version = Version.of(versionString);

        processPomFile.process(groupId, artifactId, version);

        return Response.ok().build();
    }

    private URI getProjectUri(GroupId groupId,
                              ArtifactId artifactId,
                              Version version) {

        return uriInfo.getBaseUriBuilder()
                .path(ProjectResource.class)
                .build(groupId, artifactId, version);
    }

}
