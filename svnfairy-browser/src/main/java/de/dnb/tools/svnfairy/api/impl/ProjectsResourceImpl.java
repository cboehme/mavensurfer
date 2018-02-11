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

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.dnb.tools.svnfairy.api.ProjectsResource;
import de.dnb.tools.svnfairy.api.datatypes.JsonArtifactId;
import de.dnb.tools.svnfairy.api.datatypes.JsonCollection;
import de.dnb.tools.svnfairy.api.datatypes.JsonGroupId;
import de.dnb.tools.svnfairy.api.datatypes.JsonProject;
import de.dnb.tools.svnfairy.api.datatypes.Pom;
import de.dnb.tools.svnfairy.browser.FilterProjects;
import de.dnb.tools.svnfairy.browser.Find;
import de.dnb.tools.svnfairy.browser.ImportProject;
import de.dnb.tools.svnfairy.browser.model.ArtifactId;
import de.dnb.tools.svnfairy.browser.model.GroupId;
import de.dnb.tools.svnfairy.browser.model.PomFile;
import de.dnb.tools.svnfairy.browser.model.Project;
import de.dnb.tools.svnfairy.browser.model.VersionRequirement;

@RequestScoped
public class ProjectsResourceImpl implements ProjectsResource {

    private static final Logger log = LoggerFactory.getLogger(
            ProjectsResourceImpl.class);

    @Inject
    private Find find;
    @Inject
    private FilterProjects filterProjects;
    @Inject
    private ImportProject importProject;

    @Inject
    private JsonMapper map;

    private final Base64.Decoder base64Decoder = Base64.getDecoder();

    @Override
    public Response indexPom(Pom pom) {

        final byte[] contents = base64Decoder.decode(pom.getContents());
        final PomFile pomFile = new PomFile(pom.getName(), contents);

        log.info("Received {}", pomFile.getName());
        log.debug("POM: {}", new String(pomFile.getContents(),
                StandardCharsets.UTF_8));

        importProject.from(pomFile);

        return Response.ok().build();
    }

    @Override
    public JsonCollection<JsonGroupId> listGroupIds() {

        return map.toJson(find.allGroupIds());
    }

    @Override
    public JsonCollection<JsonArtifactId> listArtifactIdsFor(String groupIdString) {

        final GroupId groupId = GroupId.of(groupIdString);

        final List<ArtifactId> artifactIds = find.artifactIdsIn(groupId);
        if (artifactIds.isEmpty()) {
            throw new NotFoundException();
        }
        return map.toJson(groupId, artifactIds);
    }

    @Override
    public JsonCollection<JsonProject> listVersionsFor(String groupIdString,
                                                       String artifactIdString,
                                                       String versionRangeString) {

        final GroupId groupId = GroupId.of(groupIdString);
        final ArtifactId artifactId = ArtifactId.of(artifactIdString);
        final VersionRequirement versionRange = versionRangeString != null
                ? VersionRequirement.of(versionRangeString)
                : VersionRequirement.allVersions();

        final List<Project> projects = find.allVersionsOf(groupId, artifactId);
        if (projects.isEmpty()) {
            throw new NotFoundException();
        }
        final List<Project> projectsInVersionRange =
                filterProjects.byVersionRange(projects, versionRange);
        return map.toJson(groupId, artifactId, projectsInVersionRange);
    }

}
