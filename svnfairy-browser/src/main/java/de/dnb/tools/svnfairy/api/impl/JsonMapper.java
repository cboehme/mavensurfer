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
import java.util.function.Function;
import java.util.function.Supplier;

import de.dnb.tools.svnfairy.api.JsonCollection;
import de.dnb.tools.svnfairy.api.JsonGroupId;
import de.dnb.tools.svnfairy.browser.model.GroupId;

public class JsonMapper {

    private Supplier<URI> listGroupIdsUri;
    private Function<GroupId, URI> listArtifactIdsUri;

    public JsonMapper withListGroupIdsUri(Supplier<URI> uriMaker) {

        listGroupIdsUri = uriMaker;
        return this;
    }

    public JsonMapper withListArtifactIdsUri(Function<GroupId, URI> uriMaker) {

        listArtifactIdsUri = uriMaker;
        return this;
    }

    public JsonCollection<JsonGroupId> toJson(List<GroupId> groupIds) {

        final List<JsonGroupId> jsonGroupIds = groupIds.stream()
                .map(this::toJson)
                .collect(toList());

        final JsonCollection<JsonGroupId> jsonCollection = new JsonCollection<>();
        jsonCollection.setId(listGroupIdsUri.get());
        jsonCollection.setTotalItems(jsonGroupIds.size());
        jsonCollection.setMember(jsonGroupIds);
        return jsonCollection;
    }

    private JsonGroupId toJson(GroupId groupId) {

        final JsonGroupId jsonGroupId = new JsonGroupId();
        jsonGroupId.setId(listArtifactIdsUri.apply(groupId));
        jsonGroupId.setGroupId(groupId.toString());
        return jsonGroupId;
    }

}
