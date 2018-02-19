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
import static de.dnb.tools.svnfairy.api.impl.Relation.parent;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;

import de.dnb.tools.svnfairy.api.ProjectResource;
import de.dnb.tools.svnfairy.api.datatypes.JsonCollection;
import de.dnb.tools.svnfairy.api.datatypes.JsonDependant;
import de.dnb.tools.svnfairy.api.datatypes.JsonDependency;
import de.dnb.tools.svnfairy.api.datatypes.JsonProject;
import de.dnb.tools.svnfairy.browser.Find;
import de.dnb.tools.svnfairy.browser.ImportProject;
import de.dnb.tools.svnfairy.browser.model.Gav;

@RequestScoped
public class ProjectResourceImpl implements ProjectResource {

    @Inject
    private Find find;
    @Inject
    private ImportProject importProject;

    @Inject
    private JsonMapper map;

    @Override
    public JsonProject getProject(String groupId,
                                  String artifactId,
                                  String version) {

        final Gav gav = Gav.of(groupId, artifactId, version);

        return find.projectWith(gav)
                .map(map::toJson)
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public void indexProject(String groupId,
                             String artifactId,
                             String version) {

        final Gav gav = Gav.of(groupId, artifactId, version);

        importProject.with(gav)
                .onErrorThrow(e -> new BadRequestException(e.getMessage(), e.getCause()));
    }

    @Override
    public JsonCollection<JsonProject> getParents(String groupId,
                                                  String artifactId,
                                                  String version) {

        final Gav gav = Gav.of(groupId, artifactId, version);

        return find.projectWith(gav)
                .map(find::parentsOf)
                .map(projects -> map.toJson(gav, parent, projects))
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public JsonCollection<JsonDependency> getDependencies(String groupId,
                                                          String artifactId,
                                                          String version) {

        final Gav gav = Gav.of(groupId, artifactId, version);

        return find.projectWith(gav)
                .map(find::dependenciesOf)
                .map(dependencies -> map.toJsonDependencies(gav, dependencies))
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public JsonCollection<JsonProject> getChildren(String groupId,
                                                   String artifactId,
                                                   String version) {

        final Gav gav = Gav.of(groupId, artifactId, version);

        return find.projectWith(gav)
                .map(find::childrenOf)
                .map(projects -> map.toJson(gav, child, projects))
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public JsonCollection<JsonDependant> getDependants(String groupId,
                                                       String artifactId,
                                                       String version) {

        final Gav gav = Gav.of(groupId, artifactId, version);

        return find.projectWith(gav)
                .map(find::dependantsOf)
                .map(projects -> map.toJson(gav, projects))
                .orElseThrow(NotFoundException::new);
    }

}
