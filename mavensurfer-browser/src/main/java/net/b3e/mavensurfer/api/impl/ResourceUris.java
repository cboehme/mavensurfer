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
package net.b3e.mavensurfer.api.impl;

import java.net.URI;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.UriInfo;

import net.b3e.mavensurfer.api.ProjectResource;
import net.b3e.mavensurfer.api.ProjectsResource;
import net.b3e.mavensurfer.browser.model.ArtifactId;
import net.b3e.mavensurfer.browser.model.Gav;
import net.b3e.mavensurfer.browser.model.GroupId;

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

    URI getParentsUri(Gav gav) {

        return uriInfo.getBaseUriBuilder()
                .path(ProjectResource.class)
                .path(ProjectResource.class, "getParents")
                .build(gav.getGroupId(), gav.getArtifactId(), gav.getVersion());
    }

    URI getDependenciesUri(Gav gav) {

        return uriInfo.getBaseUriBuilder()
                .path(ProjectResource.class)
                .path(ProjectResource.class, "getDependencies")
                .build(gav.getGroupId(), gav.getArtifactId(), gav.getVersion());
    }

    URI getChildrenUri(Gav gav) {

        return uriInfo.getBaseUriBuilder()
                .path(ProjectResource.class)
                .path(ProjectResource.class, "getChildren")
                .build(gav.getGroupId(), gav.getArtifactId(), gav.getVersion());
    }

    URI getDependantsUri(Gav gav) {

        return uriInfo.getBaseUriBuilder()
                .path(ProjectResource.class)
                .path(ProjectResource.class, "getDependants")
                .build(gav.getGroupId(), gav.getArtifactId(), gav.getVersion());
    }

    URI getProjectUri(Gav gav) {

        return uriInfo.getBaseUriBuilder()
                .path(ProjectResource.class)
                .build(gav.getGroupId(), gav.getArtifactId(), gav.getVersion());
    }

}
