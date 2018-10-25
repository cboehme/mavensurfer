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

import static java.util.stream.Collectors.toList;

import java.net.URI;
import java.util.Collection;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import net.b3e.mavensurfer.api.datatypes.JsonArtifactId;
import net.b3e.mavensurfer.api.datatypes.JsonCollection;
import net.b3e.mavensurfer.api.datatypes.JsonDependant;
import net.b3e.mavensurfer.api.datatypes.JsonDependency;
import net.b3e.mavensurfer.api.datatypes.JsonGroupId;
import net.b3e.mavensurfer.api.datatypes.JsonParent;
import net.b3e.mavensurfer.api.datatypes.JsonProject;
import net.b3e.mavensurfer.api.datatypes.JsonVersion;
import net.b3e.mavensurfer.browser.model.ArtifactId;
import net.b3e.mavensurfer.browser.model.Dependency;
import net.b3e.mavensurfer.browser.model.Gav;
import net.b3e.mavensurfer.browser.model.GroupId;
import net.b3e.mavensurfer.browser.model.Parent;
import net.b3e.mavensurfer.browser.model.Project;
import net.b3e.mavensurfer.browser.model.Version;

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

    public JsonCollection<JsonProject> toJson(GroupId groupId,
                                              ArtifactId artifactId,
                                              List<Project> projects) {

        final List<JsonProject> jsonProjects = projects.stream()
                .map(this::toJson)
                .collect(toList());
        return toJson(jsonProjects, uris.getListVersionsUri(groupId, artifactId));
    }

    private JsonVersion toJson(GroupId groupId,
                               ArtifactId artifactId,
                               Version version) {

        final JsonVersion jsonVersion = new JsonVersion();
        jsonVersion.setId(uris.getProjectUri(Gav.of(groupId, artifactId, version)));
        jsonVersion.setVersion(version.toString());
        return jsonVersion;
    }

    public JsonCollection<JsonProject> toJson(Gav gav,
                                              Relation relation,
                                              List<Project> projects) {

        final List<JsonProject> jsonProjects = projects.stream()
                .map(this::toJson)
                .collect(toList());
        return toJson(jsonProjects, makeUri(gav, relation));
    }

    private URI makeUri(Gav gav,
                        Relation relation) {

        switch (relation) {
            case parent:
                return uris.getParentsUri(gav);
            case child:
                return uris.getChildrenUri(gav);
            case dependant:
                return uris.getDependantsUri(gav);
            default:
                throw new AssertionError("Unexpected relation");
        }
    }

    public JsonProject toJson(Project project) {

        final JsonProject jsonProject = new JsonProject();
        jsonProject.setId(uris.getProjectUri(project.getGav()));
        jsonProject.setArtifactId(project.getArtifactId().toString());
        jsonProject.setGroupId(project.getGroupId().toString());
        jsonProject.setVersion(project.getVersion().toString());
        project.getParent()
                .map(this::toJson)
                .ifPresent(jsonProject::setParent);
        jsonProject.setName(project.getName());
        jsonProject.setDescription(project.getDescription());
        jsonProject.setDependencies(uris.getDependenciesUri(project.getGav()));
        jsonProject.setChildren(uris.getChildrenUri(project.getGav()));
        jsonProject.setDependants(uris.getDependantsUri(project.getGav()));
        return jsonProject;
    }

    private JsonParent toJson(Parent parent) {

        final JsonParent jsonParent = new JsonParent();
        jsonParent.setGroupId(parent.getGroupId().toString());
        jsonParent.setArtifactId(parent.getArtifactId().toString());
        jsonParent.setVersionRange(parent.getVersionRange().toString());
        return jsonParent;
    }

    public JsonCollection<JsonDependency> toJsonDependencies(Gav gav,
                                                             Collection<Dependency> dependencies) {

        final List<JsonDependency> jsonDependencies = dependencies.stream()
                .map(this::toJson)
                .collect(toList());
        return toJson(jsonDependencies, uris.getDependenciesUri(gav));
    }

    private JsonDependency toJson(Dependency dependency) {

        final JsonDependency jsonDependency = new JsonDependency();
        jsonDependency.setGroupId(dependency.getGroupId().toString());
        jsonDependency.setArtifactId(dependency.getArtifactId().toString());
        jsonDependency.setVersionRange(dependency.getVersion().toString());
        jsonDependency.setClassifier(dependency.getClassifier().toString());
        jsonDependency.setType(dependency.getType().toString());
        jsonDependency.setScope(dependency.getScope().toString());
        jsonDependency.setOptional(dependency.isOptional());
        return jsonDependency;
    }

    public JsonCollection<JsonDependant> toJson(Gav gav,
                                                List<Project> projects) {

        final List<JsonDependant> jsonDependants = projects.stream()
                .map(this::toJsonDependant)
                .collect(toList());
        return toJson(jsonDependants, uris.getDependantsUri(gav));
    }

    private JsonDependant toJsonDependant(Project project) {

        final JsonDependant jsonDependant = new JsonDependant();
        jsonDependant.setProject(toJson(project));
        final Dependency dependency = project.getDependencies().get(0);
        jsonDependant.setClassifier(dependency.getClassifier().toString());
        jsonDependant.setType(dependency.getType().toString());
        jsonDependant.setScope(dependency.getScope().toString());
        jsonDependant.setOptional(dependency.isOptional());
        return jsonDependant;
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
