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
package de.dnb.tools.svnfairy.api.datatypes;

import java.net.URI;

public class JsonProject extends  Referable {

    private String groupId;
    private String artifactId;
    private String version;
    private URI parents;
    private URI children;
    private URI dependants;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public URI getParents() {
        return parents;
    }

    public void setParents(URI parents) {
        this.parents = parents;
    }

    public URI getChildren() {
        return children;
    }

    public void setChildren(URI children) {
        this.children = children;
    }

    public URI getDependants() {
        return dependants;
    }

    public void setDependants(URI dependants) {
        this.dependants = dependants;
    }

}
