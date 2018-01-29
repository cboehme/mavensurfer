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
package de.dnb.tools.svnfairy.browser.model;

import java.util.Objects;

import de.dnb.tools.svnfairy.browser.Util;

public final class Parent {

    private final GroupId groupId;
    private final ArtifactId artifactId;
    private final VersionRequirement version;

    private Parent(GroupId groupId,
                   ArtifactId artifactId,
                   VersionRequirement version) {

        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
    }

    public static Parent of(GroupId groupdId,
                            ArtifactId artifactId,
                            VersionRequirement version) {

        return new Parent(groupdId, artifactId, version);
    }

    public GroupId getGroupId() {
        return groupId;
    }

    public ArtifactId getArtifactId() {
        return artifactId;
    }

    public VersionRequirement getVersion() {
        return version;
    }

    @Override
    public boolean equals(Object obj) {

        return Util.equals(this, obj, (a, b) ->
                Objects.equals(a.groupId, b.groupId)
                && Objects.equals(a.artifactId, b.artifactId)
                && Objects.equals(a.version, b.version));
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, artifactId, version);
    }

    @Override
    public String toString() {
        return groupId + ":" + artifactId + ":" + version;
    }

}
