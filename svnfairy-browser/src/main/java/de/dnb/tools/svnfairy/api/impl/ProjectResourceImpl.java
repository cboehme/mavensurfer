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

import static de.dnb.tools.svnfairy.api.impl.Relation.child;
import static de.dnb.tools.svnfairy.api.impl.Relation.dependent;
import static de.dnb.tools.svnfairy.api.impl.Relation.parent;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;

import de.dnb.tools.svnfairy.api.ProjectResource;
import de.dnb.tools.svnfairy.api.datatypes.JsonCollection;
import de.dnb.tools.svnfairy.api.datatypes.JsonProject;
import de.dnb.tools.svnfairy.browser.FindChildren;
import de.dnb.tools.svnfairy.browser.FindDependents;
import de.dnb.tools.svnfairy.browser.FindParents;
import de.dnb.tools.svnfairy.browser.ProcessPomFile;
import de.dnb.tools.svnfairy.browser.QueryProjects;
import de.dnb.tools.svnfairy.browser.model.ArtifactId;
import de.dnb.tools.svnfairy.browser.model.Gav;
import de.dnb.tools.svnfairy.browser.model.GroupId;
import de.dnb.tools.svnfairy.browser.model.Version;

@RequestScoped
public class ProjectResourceImpl implements ProjectResource {

    @Inject
    private ProcessPomFile processPomFile;
    @Inject
    private QueryProjects queryProjects;
    @Inject
    private FindParents findParents;
    @Inject
    private FindChildren findChildren;
    @Inject
    private FindDependents findDependents;

    @Inject
    private JsonMapper map;

    @Override
    public JsonProject getProject(String groupId,
                                  String artifactId,
                                  String version) {

        final Gav gav = Gav.of(groupId, artifactId, version);

        return queryProjects.getProject(gav)
                .map(map::toJson)
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public Response indexProject(String groupIdString,
                                 String artifactIdString,
                                 String versionString) {

        final GroupId groupId = GroupId.of(groupIdString);
        final ArtifactId artifactId = ArtifactId.of(artifactIdString);
        final Version version = Version.of(versionString);

        processPomFile.process(groupId, artifactId, version);

        return Response.ok().build();
    }

    @Override
    public JsonCollection getParents(String groupId,
                                     String artifactId,
                                     String version) {

        final Gav gav = Gav.of(groupId, artifactId, version);

        return queryProjects.getProject(gav)
                .map(findParents::of)
                .map(projects -> map.toJson(gav, parent, projects))
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public JsonCollection getChildren(String groupId,
                                      String artifactId,
                                      String version) {

        final Gav gav = Gav.of(groupId, artifactId, version);

        return queryProjects.getProject(gav)
                .map(findChildren::of)
                .map(projects -> map.toJson(gav, child, projects))
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public JsonCollection getDependents(String groupId,
                                        String artifactId,
                                        String version) {

        final Gav gav = Gav.of(groupId, artifactId, version);

        return queryProjects.getProject(gav)
                .map(findDependents::of)
                .map(projects -> map.toJson(gav, dependent, projects))
                .orElseThrow(NotFoundException::new);
    }

}
