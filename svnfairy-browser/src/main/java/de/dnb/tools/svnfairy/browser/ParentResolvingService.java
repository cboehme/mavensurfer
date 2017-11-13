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
package de.dnb.tools.svnfairy.browser;

import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.dnb.tools.svnfairy.browser.model.Gav;
import de.dnb.tools.svnfairy.browser.model.Parent;
import de.dnb.tools.svnfairy.browser.model.Project;

public class ParentResolvingService {

    private static final Logger log =
            LoggerFactory.getLogger(ParentResolvingService.class);

    public void resolveProjects(Collection<Project> projects) {

        final Collection<Project> unresolvedProjects =
                findProjectsWithIncompleteCoordinates(projects);

        while (!unresolvedProjects.isEmpty()) {
            final int initialCount = unresolvedProjects.size();
            tryResolveProjects(projects, unresolvedProjects);
            if (initialCount == unresolvedProjects.size()) {
                log.warn("Could not resolve all projects:");
                unresolvedProjects.forEach(p -> log.info(p.toString()));
                return;
            }
        }
    }

    private void tryResolveProjects(Collection<Project> projects,
                                    Collection<Project> unresolvedProjects) {

        final Iterator<Project> iter = unresolvedProjects.iterator();
        while (iter.hasNext()) {
            final Project project = iter.next();
            if (!project.getParent().isPresent()) {
                log.warn("Project has incomplete coordinates but defines no parent: {}",
                        project);
                iter.remove();
                continue;
            }
            final Parent parent = project.getParent().get();
            final Gav gav = Gav.of(parent.getGroupId(), parent.getArtifactId(),
                    parent.getVersion());
            final Optional<Project> parentProject = findByGav(projects, gav);
            if (parentProject.isPresent()) {
                if (!project.getGroupId().isPresent()) {
                    project.setGroupId(parentProject.get().getGroupId().get());
                }
                if (!project.getVersion().isPresent()) {
                    project.setVersion(parentProject.get().getVersion().get());
                }
                iter.remove();
            }
        }
    }

    private Collection<Project> findProjectsWithIncompleteCoordinates(
            Collection<Project> projects) {

        return projects.stream()
                .filter(Project::hasIncompleteCoordinates)
                .collect(toList());
    }

    private Optional<Project> findByGav(Collection<Project> projects,
                                        Gav gav) {

        return projects.stream()
                .filter(p -> gav.equals(p.getGav().orElse(null)))
                .findFirst();
    }

}
