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
package de.dnb.tools.svnfairy.browser.model

import spock.lang.Specification

/**
 * @author Christoph Böhme
 */
class VersionRequirementTest extends Specification {

    def "should match same ersion" () {
        given:
        VersionRequirement versionRequirement = VersionRequirement.of("1.0")
        Version version = Version.of("1.0")

        when:
        def matches = versionRequirement.containsVersion(version)

        then:
        matches
    }

    def "should not match different versions" () {
        given:
        VersionRequirement versionRequirement = VersionRequirement.of("1.0")
        Version version = Version.of("2.0")

        when:
        def matches = versionRequirement.containsVersion(version)

        then:
        !matches
    }

    def "should match version in version range" () {
        given:
        VersionRequirement versionRequirement = VersionRequirement.of("[1.0,3.0]")
        Version version = Version.of("2.0")

        when:
        def matches = versionRequirement.containsVersion(version)

        then:
        matches
    }


    def "should not match version outside version range" () {
        given:
        VersionRequirement versionRequirement = VersionRequirement.of("[1.0,3.0]")
        Version version = Version.of("4.0")

        when:
        def matches = versionRequirement.containsVersion(version)

        then:
        !matches
    }

}
