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

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.UriInfo;

import de.dnb.tools.svnfairy.api.ProjectResource;
import de.dnb.tools.svnfairy.api.ProjectsResource;
import de.dnb.tools.svnfairy.browser.model.ArtifactId;
import de.dnb.tools.svnfairy.browser.model.GroupId;
import de.dnb.tools.svnfairy.browser.model.Version;

@RequestScoped
public class ResourceUris {

    @Inject
    private UriInfo uriInfo;

    URI getListGroupIdsUri() {

        return uriInfo.getBaseUriBuilder()
                .path(ProjectsResource.class)
                .build();
    }

    URI getListArtifactIdsUri(GroupId groupId) {

        return uriInfo.getBaseUriBuilder()
                .path(ProjectsResource.class)
                .path(ProjectsResource.class, "listArtifactIdsFor")
                .build(groupId);
    }

    URI getListVersionsUri(GroupId groupId,
                           ArtifactId artifactId) {

        return uriInfo.getBaseUriBuilder()
                .path(ProjectsResource.class)
                .path(ProjectsResource.class, "listVersionsFor")
                .build(groupId, artifactId);
    }

    public URI getParentsUri(GroupId groupId,
                             ArtifactId artifactId,
                             Version version) {

        return uriInfo.getBaseUriBuilder()
                .path(ProjectResource.class)
                .path(ProjectResource.class, "getParents")
                .build(groupId, artifactId, version);
    }

    public URI getChildrenUri(GroupId groupId,
                              ArtifactId artifactId,
                              Version version) {

        return uriInfo.getBaseUriBuilder()
                .path(ProjectResource.class)
                .path(ProjectResource.class, "getChildren")
                .build(groupId, artifactId, version);
    }

    public URI getDependentsUri(GroupId groupId,
                              ArtifactId artifactId,
                              Version version) {

        return uriInfo.getBaseUriBuilder()
                .path(ProjectResource.class)
                .path(ProjectResource.class, "getDependents")
                .build(groupId, artifactId, version);
    }

    URI getProjectUri(GroupId groupId,
                      ArtifactId artifactId,
                      Version version) {

        return uriInfo.getBaseUriBuilder()
                .path(ProjectResource.class)
                .build(groupId, artifactId, version);
    }
}
