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

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import de.dnb.tools.svnfairy.browser.db.JpaRepository;
import de.dnb.tools.svnfairy.browser.model.Parent;
import de.dnb.tools.svnfairy.browser.model.Project;

@ApplicationScoped
public class FindParents {

    @Inject
    private JpaRepository repository;

    public List<Project> of(Project project) {

        if (!project.getParent().isPresent()) {
            return emptyList();
        }
        final Parent parent = project.getParent().get();
        return repository.findProjectsWith(parent.getGroupId(), parent.getArtifactId())
                .stream()
                .filter(p -> parent.getVersion().containsVersion(p.getVersion()))
                .collect(toList());
    }

}
