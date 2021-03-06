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

public class Reference {

    private final GroupId groupId;
    private final ArtifactId artifactId;
    private final VersionRequirement versionRange;

    protected Reference(GroupId groupId,
                        ArtifactId artifactId,
                        VersionRequirement versionRange) {

        this.groupId = groupId;
        this.artifactId = artifactId;
        this.versionRange = versionRange;
    }

    public GroupId getGroupId() {
        return groupId;
    }

    public ArtifactId getArtifactId() {
        return artifactId;
    }

    public VersionRequirement getVersionRange() {
        return versionRange;
    }

    @Override
    public boolean equals(Object obj) {

        return Util.equals(this, obj, (a, b) ->
                Objects.equals(a.groupId, b.groupId)
                && Objects.equals(a.artifactId, b.artifactId)
                && Objects.equals(a.versionRange, b.versionRange));
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, artifactId, versionRange);
    }

    @Override
    public String toString() {
        return groupId + ":" + artifactId + ":" + versionRange;
    }

}
