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

import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.List;

import net.b3e.mavensurfer.browser.model.Project;
import net.b3e.mavensurfer.browser.model.VersionRequirement;

public class FilterProjects {

    public List<Project> byVersionRange(Collection<Project> projects,
                                        VersionRequirement versionRange) {

        return projects.stream()
                .filter(p -> versionRange.containsVersion(p.getVersion()))
                .collect(toList());
    }

}
