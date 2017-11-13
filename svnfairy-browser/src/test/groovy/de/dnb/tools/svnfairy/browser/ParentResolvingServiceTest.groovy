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
package de.dnb.tools.svnfairy.browser

import de.dnb.tools.svnfairy.browser.model.*
import spock.lang.Specification

class ParentResolvingServiceTest extends Specification {

    List<Project> projects = new ArrayList<>()

    ParentResolvingService resolvingService = new ParentResolvingService()

    def "should keep projects with full coordinates" () {
        given:
        def project = new Project("test/pom.xml");
        project.groupId = GroupId.of("de.dnb.tools")
        project.artifactId = ArtifactId.of("test")
        project.version = Version.of("1.0.0")
        projects.add(project)

        when:
        resolvingService.resolveProjects(projects)

        then:
        projects.get(0).getGroupId().get().equals(GroupId.of("de.dnb.tools"))
        projects.get(0).getArtifactId().equals(ArtifactId.of("test"))
        projects.get(0).getVersion().get().equals(Version.of("1.0.0"))
    }

    def "should copy group-id and version from parent" () {
        given:
        def parent = new Project("parent/pom.xml")
        parent.groupId = GroupId.of("de.dnb.tools")
        parent.artifactId = ArtifactId.of("parent")
        parent.version = Version.of("1.0.0")

        def child = new Project("child/pom.xml")
        child.artifactId = ArtifactId.of("child")
        child.setParent(Parent.of(
                GroupId.of("de.dnb.tools"),
                ArtifactId.of("parent"),
                Version.of("1.0.0")))

        projects.add(parent)
        projects.add(child)

        when:
        resolvingService.resolveProjects(projects)

        then:
        projects.get(1).getGroupId().get().equals(GroupId.of("de.dnb.tools"))
        projects.get(1).getArtifactId().equals(ArtifactId.of("child"))
        projects.get(1).getVersion().get().equals(Version.of("1.0.0"))
    }

    def "should resolve multiple parents in reverse order" () {
        given:
        def grandparent = new Project("grandparent/pom.xml")
        grandparent.groupId = GroupId.of("de.dnb.tools")
        grandparent.artifactId = ArtifactId.of("grandparent")
        grandparent.version = Version.of("1.0.0")

        def parent = new Project("parent/pom.xml")
        parent.artifactId = ArtifactId.of("parent")
        parent.setParent(Parent.of(
                GroupId.of("de.dnb.tools"),
                ArtifactId.of("grandparent"),
                Version.of("1.0.0")))

        def child = new Project("child/pom.xml")
        child.artifactId = ArtifactId.of("child")
        child.setParent(Parent.of(
                GroupId.of("de.dnb.tools"),
                ArtifactId.of("parent"),
                Version.of("1.0.0")))

        projects.add(child)
        projects.add(parent)
        projects.add(grandparent)

        when:
        resolvingService.resolveProjects(projects)

        then:
        projects.get(0).getGroupId().get().equals(GroupId.of("de.dnb.tools"))
        projects.get(0).getArtifactId().equals(ArtifactId.of("child"))
        projects.get(0).getVersion().get().equals(Version.of("1.0.0"))

        projects.get(1).getGroupId().get().equals(GroupId.of("de.dnb.tools"))
        projects.get(1).getArtifactId().equals(ArtifactId.of("parent"))
        projects.get(1).getVersion().get().equals(Version.of("1.0.0"))
    }

    def "should ignore projects with incomplete coordinates and no parent" () {
        given:
        def project = new Project("test/pom.xml");
        project.artifactId = ArtifactId.of("test")
        projects.add(project)

        when:
        resolvingService.resolveProjects(projects)

        then:
        !projects.get(0).getGroupId().isPresent()
        projects.get(0).getArtifactId().equals(ArtifactId.of("test"))
        !projects.get(0).getVersion().isPresent()
    }

    def "should ignore projects whose parent cannot be resolved" () {
        given:
        def child = new Project("child/pom.xml")
        child.artifactId = ArtifactId.of("child")
        child.setParent(Parent.of(
                GroupId.of("de.dnb.tools"),
                ArtifactId.of("parent"),
                Version.of("1.0.0")))

        projects.add(child)

        when:
        resolvingService.resolveProjects(projects)

        then:
        !projects.get(0).getGroupId().isPresent()
        projects.get(0).getArtifactId().equals(ArtifactId.of("child"))
        !projects.get(0).getVersion().isPresent()
    }

}
