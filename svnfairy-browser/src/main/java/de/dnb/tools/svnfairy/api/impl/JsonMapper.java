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
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import de.dnb.tools.svnfairy.api.datatypes.JsonCollection;
import de.dnb.tools.svnfairy.api.datatypes.JsonArtifactId;
import de.dnb.tools.svnfairy.api.datatypes.JsonGroupId;
import de.dnb.tools.svnfairy.api.datatypes.JsonVersion;
import de.dnb.tools.svnfairy.browser.model.ArtifactId;
import de.dnb.tools.svnfairy.browser.model.GroupId;
import de.dnb.tools.svnfairy.browser.model.Version;

public class JsonMapper {

    private Supplier<URI> listGroupIdsUri;
    private Function<GroupId, URI> listArtifactIdsUri;
    private BiFunction<GroupId, ArtifactId, URI> listVersionsUri;
    private TriFunction<GroupId, ArtifactId, Version, URI> projectUri;

    public void setListGroupIdsUri(Supplier<URI> uriMaker) {

        listGroupIdsUri = uriMaker;
    }

    public void setListArtifactIdsUri(Function<GroupId, URI> uriMaker) {

        listArtifactIdsUri = uriMaker;
    }

    public void setListVersionsUri(BiFunction<GroupId, ArtifactId, URI> listVersionsUri) {

        this.listVersionsUri = listVersionsUri;
    }

    public void setProjectUri(TriFunction<GroupId, ArtifactId, Version, URI> projectUri) {

        this.projectUri = projectUri;
    }

    public JsonCollection<JsonGroupId> groupIdsToJson(List<GroupId> groupIds) {

        final List<JsonGroupId> jsonGroupIds = groupIds.stream()
                .map(this::groupIdToJson)
                .collect(toList());
        return collectionToJson(jsonGroupIds, listGroupIdsUri.get());
    }

    private JsonGroupId groupIdToJson(GroupId groupId) {

        final JsonGroupId jsonGroupId = new JsonGroupId();
        jsonGroupId.setId(listArtifactIdsUri.apply(groupId));
        jsonGroupId.setGroupId(groupId.toString());
        return jsonGroupId;
    }

    public JsonCollection<JsonArtifactId> artifactIdsToJson(GroupId groupId,
                                                            List<ArtifactId> artifactIds) {

        final List<JsonArtifactId> jsonArtifactIds = artifactIds.stream()
                .map(artifactId -> artifactIdToJson(groupId, artifactId))
                .collect(toList());
        return collectionToJson(jsonArtifactIds, listArtifactIdsUri.apply(groupId));
    }

    private JsonArtifactId artifactIdToJson(GroupId groupId,
                                            ArtifactId artifactId) {

        final JsonArtifactId jsonArtifactId = new JsonArtifactId();
        jsonArtifactId.setId(listVersionsUri.apply(groupId, artifactId));
        jsonArtifactId.setArtifactId(artifactId.toString());
        return jsonArtifactId;
    }

    public JsonCollection<JsonVersion> versionsToJson(GroupId groupId,
                                                      ArtifactId artifactId,
                                                      List<Version> versions) {

        final List<JsonVersion> jsonVersions = versions.stream()
                .map(version -> versionToJson(groupId, artifactId, version))
                .collect(toList());
        return collectionToJson(jsonVersions, listVersionsUri.apply(groupId, artifactId));
    }

    private JsonVersion versionToJson(GroupId groupId,
                                      ArtifactId artifactId,
                                      Version version) {

        final JsonVersion jsonVersion = new JsonVersion();
        jsonVersion.setId(projectUri.apply(groupId, artifactId, version));
        jsonVersion.setVersion(version.toString());
        return jsonVersion;
    }

    private <E> JsonCollection<E> collectionToJson(List<E> collection,
                                                   URI id) {

        final JsonCollection<E> jsonCollection = new JsonCollection<>();
        jsonCollection.setId(id);
        jsonCollection.setTotalItems(collection.size());
        jsonCollection.setMember(collection);
        return jsonCollection;
    }
}
