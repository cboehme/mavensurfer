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
package net.b3e.mavensurfer.browser;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import net.b3e.mavensurfer.browser.db.JpaRepository;
import net.b3e.mavensurfer.browser.model.ArtifactId;
import net.b3e.mavensurfer.browser.model.Dependency;
import net.b3e.mavensurfer.browser.model.Gav;
import net.b3e.mavensurfer.browser.model.GroupId;
import net.b3e.mavensurfer.browser.model.Parent;
import net.b3e.mavensurfer.browser.model.Project;
import net.b3e.mavensurfer.browser.model.VersionRequirement;

@ApplicationScoped
public class Find {

    @Inject
    private JpaRepository repository;

    public java.util.List<GroupId> allGroupIds() {

        return repository.findGroupIds();
    }

    public java.util.List<ArtifactId> artifactIdsIn(GroupId groupId) {

        return repository.findArtifactIdsIn(groupId);
    }

    public java.util.List<Project> allVersionsOf(GroupId groupId,
                                                 ArtifactId artifactId) {

        return repository.findProjectsWith(groupId, artifactId);
    }

    public Optional<Project> projectWith(Gav gav) {

        return repository.getByGav(gav.getGroupId(), gav.getArtifactId(),
                gav.getVersion());
    }

    public List<Project> childrenOf(Project parent) {

        return repository.getChildProjectsOf(parent).stream()
                .filter(child -> child.getParent()
                        .map(Parent::getVersionRange)
                        .map(versionRange -> versionRange.containsVersion(parent.getVersion()))
                        .orElse(false))
                .collect(toList());
    }

    public List<Project> dependantsOf(Project project) {

        return repository.getDependentProjects(project).stream()
                .filter(p -> p.getDependencies().get(0).getVersion().containsVersion(project.getVersion()))
                .collect(toList());
    }

    public Collection<Dependency> dependenciesOf(Project project) {

        return repository.getDependenciesOf(project);
    }

    public List<Project> parentsOf(Project project) {

        if (!project.getParent().isPresent()) {
            return emptyList();
        }
        final Parent parent = project.getParent().get();
        return findMatchingProjects(parent.getGroupId(), parent.getArtifactId(),
                parent.getVersionRange());
    }

    private List<Project> findMatchingProjects(GroupId groupId,
                                               ArtifactId artifactId,
                                               VersionRequirement versionRange) {

        return repository.findProjectsWith(groupId, artifactId).stream()
                .filter(p -> versionRange.containsVersion(p.getVersion()))
                .collect(toList());
    }

}
