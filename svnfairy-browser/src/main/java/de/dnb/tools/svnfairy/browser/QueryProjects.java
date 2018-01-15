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
package de.dnb.tools.svnfairy.browser;

import static java.util.stream.Collectors.toList;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import de.dnb.tools.svnfairy.browser.db.JpaRepository;
import de.dnb.tools.svnfairy.browser.model.ArtifactId;
import de.dnb.tools.svnfairy.browser.model.GroupId;
import de.dnb.tools.svnfairy.browser.model.Project;
import de.dnb.tools.svnfairy.browser.model.Version;

@ApplicationScoped
public class QueryProjects {

    @Inject
    private JpaRepository repository;

    public List<GroupId> getGroupIds() {

        return repository.findGroupIds();
    }

    public List<ArtifactId> getArtifactIdsIn(GroupId groupId) {

        return repository.findArtifactIdsIn(groupId);
    }

    public List<Version> getVersionsOf(GroupId groupId, ArtifactId artifactId) {

        return repository.findProjectsWith(groupId, artifactId).stream()
                .map(Project::getVersion)
                .collect(toList());
    }

}
