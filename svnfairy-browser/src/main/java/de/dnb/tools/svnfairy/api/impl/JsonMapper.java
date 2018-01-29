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

import static java.util.stream.Collectors.toList;

import java.net.URI;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import de.dnb.tools.svnfairy.api.datatypes.JsonArtifactId;
import de.dnb.tools.svnfairy.api.datatypes.JsonCollection;
import de.dnb.tools.svnfairy.api.datatypes.JsonGroupId;
import de.dnb.tools.svnfairy.api.datatypes.JsonProject;
import de.dnb.tools.svnfairy.api.datatypes.JsonVersion;
import de.dnb.tools.svnfairy.browser.model.ArtifactId;
import de.dnb.tools.svnfairy.browser.model.GroupId;
import de.dnb.tools.svnfairy.browser.model.Project;
import de.dnb.tools.svnfairy.browser.model.Version;

@ApplicationScoped
public class JsonMapper {

    @Inject
    private ResourceUris uris;

    public JsonCollection<JsonGroupId> toJson(List<GroupId> groupIds) {

        final List<JsonGroupId> jsonGroupIds = groupIds.stream()
                .map(this::toJson)
                .collect(toList());
        return toJson(jsonGroupIds, uris.getListGroupIdsUri());
    }

    private JsonGroupId toJson(GroupId groupId) {

        final JsonGroupId jsonGroupId = new JsonGroupId();
        jsonGroupId.setId(uris.getListArtifactIdsUri(groupId));
        jsonGroupId.setGroupId(groupId.toString());
        return jsonGroupId;
    }

    public JsonCollection<JsonArtifactId> toJson(GroupId groupId,
                                                 List<ArtifactId> artifactIds) {

        final List<JsonArtifactId> jsonArtifactIds = artifactIds.stream()
                .map(artifactId -> toJson(groupId, artifactId))
                .collect(toList());
        return toJson(jsonArtifactIds, uris.getListArtifactIdsUri(groupId));
    }

    private JsonArtifactId toJson(GroupId groupId,
                                  ArtifactId artifactId) {

        final JsonArtifactId jsonArtifactId = new JsonArtifactId();
        jsonArtifactId.setId(uris.getListVersionsUri(groupId, artifactId));
        jsonArtifactId.setArtifactId(artifactId.toString());
        return jsonArtifactId;
    }

    public JsonCollection<JsonVersion> toJson(GroupId groupId,
                                              ArtifactId artifactId,
                                              List<Version> versions) {

        final List<JsonVersion> jsonVersions = versions.stream()
                .map(version -> toJson(groupId, artifactId, version))
                .collect(toList());
        return toJson(jsonVersions, uris.getListVersionsUri(groupId, artifactId));
    }

    private JsonVersion toJson(GroupId groupId,
                               ArtifactId artifactId,
                               Version version) {

        final JsonVersion jsonVersion = new JsonVersion();
        jsonVersion.setId(uris.getProjectUri(groupId, artifactId, version));
        jsonVersion.setVersion(version.toString());
        return jsonVersion;
    }

    public JsonProject toJson(Project project) {

        final JsonProject jsonProject = new JsonProject();
        jsonProject.setId(uris.getProjectUri(project.getGroupId(),
                project.getArtifactId(), project.getVersion()));
        jsonProject.setArtifactId(project.getArtifactId().toString());
        jsonProject.setGroupId(project.getGroupId().toString());
        jsonProject.setVersion(project.getVersion().toString());
        return jsonProject;
    }

    private <E> JsonCollection<E> toJson(List<E> collection,
                                         URI id) {

        final JsonCollection<E> jsonCollection = new JsonCollection<>();
        jsonCollection.setId(id);
        jsonCollection.setTotalItems(collection.size());
        jsonCollection.setMember(collection);
        return jsonCollection;
    }

}
