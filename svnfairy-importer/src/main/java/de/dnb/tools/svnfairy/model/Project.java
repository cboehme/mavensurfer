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
package de.dnb.tools.svnfairy.model;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import de.dnb.tools.svnfairy.Util;

public class Project {

    private final String file;

    private Parent parent;

    private GroupId groupId;
    private ArtifactId artifactId;
    private Version version;
    private Packaging packaging;

    private List<Dependency> dependencies = new ArrayList<>();

    public Project(String file) {
        this.file = file;
    }

    public String getFile() {
        return file;
    }

    public Optional<Parent> getParent() {
        return Optional.ofNullable(parent);
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }

    public Optional<GroupId> getGroupId() {
        return Optional.ofNullable(groupId);
    }

    public void setGroupId(GroupId groupId) {
        this.groupId = groupId;
    }

    public ArtifactId getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(ArtifactId artifactId) {
        requireNonNull(artifactId);

        this.artifactId = artifactId;
    }

    public Optional<Version> getVersion() {
        return Optional.ofNullable(version);
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    public boolean hasIncompleteCoordinates() {
        return groupId == null || version == null;
    }

    public Optional<Gav> getGav() {
        if (groupId == null || version == null) {
            return Optional.empty();
        }
        return Optional.of(Gav.of(groupId, artifactId, version));
    }

    public Packaging getPackaging() {
        return packaging;
    }

    public void setPackaging(Packaging packaging) {
        this.packaging = packaging;
    }

    public List<Dependency> getDependencies() {
        return Collections.unmodifiableList(dependencies);
    }

    public void addDependency(Dependency dependency) {
        dependencies.add(dependency);
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
        return groupId + ":" + artifactId + ":" + version;
    }

}
