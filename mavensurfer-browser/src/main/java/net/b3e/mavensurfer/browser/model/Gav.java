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
package net.b3e.mavensurfer.browser.model;

import java.util.Objects;

import net.b3e.mavensurfer.browser.Util;

/**
 * Combines {@link GroupId}, {@link ArtifactId} and {@link Version}.
 */
public class Gav {

    private final GroupId groupId;
    private final ArtifactId artifactId;
    private final Version version;

    private Gav(GroupId groupId,
               ArtifactId artifactId,
               Version version) {

        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
    }

    public static Gav of(GroupId groupId,
                         ArtifactId artifactId,
                         Version version) {

        return new Gav(groupId, artifactId, version);
    }

    public static Gav of(String groupId,
                         String artifactId,
                         String version) {

        return of(GroupId.of(groupId), ArtifactId.of(artifactId),
                Version.of(version));
    }

    public GroupId getGroupId() {
        return groupId;
    }

    public ArtifactId getArtifactId() {
        return artifactId;
    }

    public Version getVersion() {
        return version;
    }

    @Override
    public boolean equals(Object obj) {

        return Util.equals(this, obj, (a, b) ->
                Objects.equals(a.groupId, b.groupId) &&
                Objects.equals(a.artifactId, b.artifactId) &&
                Objects.equals(a.version, b.version));
    }

    @Override
    public int hashCode() {

        return Objects.hash(groupId, artifactId, version);
    }

    @Override
    public String toString() {

        return String.format("%s:%s:%s", groupId, artifactId, version);
    }

}
