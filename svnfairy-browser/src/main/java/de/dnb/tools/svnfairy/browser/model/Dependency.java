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

import static java.util.Objects.requireNonNull;

public class Dependency {

    private GroupId groupId;
    private ArtifactId artifactId;
    private VersionRequirement version;
    private Classifier classifier = Classifier.none();
    private Type type = Type.defaultType();
    private Scope scope = Scope.COMPILE;
    private boolean optional = false;

    public GroupId getGroupId() {
        return groupId;
    }

    public void setGroupId(GroupId groupId) {
        this.groupId = groupId;
    }

    public ArtifactId getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(ArtifactId artifactId) {
        this.artifactId = artifactId;
    }

    public VersionRequirement getVersion() {
        return version;
    }

    public void setVersion(VersionRequirement version) {
        this.version = version;
    }

    public Classifier getClassifier() {
        return classifier;
    }

    public void setClassifier(Classifier classifier) {

        requireNonNull(classifier);
        this.classifier = classifier;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {

        requireNonNull(type);
        this.type = type;
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {

        requireNonNull(scope);
        this.scope = scope;
    }

    public boolean isOptional() {
        return optional;
    }

    public void setOptional(boolean optional) {
        this.optional = optional;
    }

}
