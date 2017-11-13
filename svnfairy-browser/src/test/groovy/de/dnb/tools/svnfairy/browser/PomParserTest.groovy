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

import de.dnb.tools.svnfairy.browser.model.ArtifactId
import de.dnb.tools.svnfairy.browser.model.Classifier
import de.dnb.tools.svnfairy.browser.model.GroupId
import de.dnb.tools.svnfairy.browser.model.PomFile
import de.dnb.tools.svnfairy.browser.model.Scope
import de.dnb.tools.svnfairy.browser.model.Type
import de.dnb.tools.svnfairy.browser.model.Version
import de.dnb.tools.svnfairy.browser.model.VersionRequirement
import spock.lang.Specification

import java.nio.charset.StandardCharsets

class PomParserTest extends Specification {

    def xml
    def project

    PomParser pomParser = new PomParser()

    def "should not set parent if pom declares no parent" () {
        given:
        xml = """
        <project>
            <artifactId>test</artifactId>
        </project>""".getBytes(StandardCharsets.UTF_8)

        when:
        project = pomParser.parsePom(new PomFile("", xml))

        then:
        !project.parent.isPresent()
    }

    def "should set parent if pom declares parent" () {
        given:
        xml = """
            <project>
                <parent>
                    <groupId>de.dnb.tools</groupId>
                    <artifactId>test</artifactId>
                    <version>1.0</version>
                </parent>
                <artifactId>test</artifactId>
            </project>""".getBytes(StandardCharsets.UTF_8)

        when:
        project = pomParser.parsePom(new PomFile("", xml))

        then:
        project.parent.isPresent()
        with(project.parent.get()) {
            groupId.equals(GroupId.of("de.dnb.tools"))
            artifactId.equals(ArtifactId.of("test"))
            version.equals(Version.of("1.0"))
        }
    }

    def "should add dependency" () {
        given:
        xml = """
            <project>
                <artifactId>test</artifactId>
                <dependencies>
                    <dependency>
                        <groupId>de.dnb.tools</groupId>
                        <artifactId>test</artifactId>
                        <version>1.0</version>
                        <classifier>classy</classifier>
                        <type>jar</type>
                        <scope>compile</scope>
                        <optional>false</optional>
                    </dependency>
                </dependencies>
            </project>""".getBytes(StandardCharsets.UTF_8)

        when:
        project = pomParser.parsePom(new PomFile("", xml))

        then:
        project.dependencies.size() == 1
        with(project.dependencies.get(0)) {
            groupId.equals(GroupId.of("de.dnb.tools"))
            artifactId.equals(ArtifactId.of("test"))
            version.equals(VersionRequirement.of("1.0"))
            classifier.equals(Classifier.of("classy"))
            type.equals(Type.of("jar"))
            scope == Scope.COMPILE
            !optional
        }
    }

}
