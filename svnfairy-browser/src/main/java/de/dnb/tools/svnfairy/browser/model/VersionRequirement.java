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
package de.dnb.tools.svnfairy.browser.model;

import java.util.Objects;

import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.artifact.versioning.VersionRange;

import de.dnb.tools.svnfairy.browser.Util;

public final class VersionRequirement {

    private final String versionRequirement;

    private VersionRequirement(String versionRequirement) {
        this.versionRequirement = versionRequirement;
    }

    public static VersionRequirement of(String versionRequirement) {
        if (versionRequirement == null) {
            return null;
        }
        return new VersionRequirement(versionRequirement);
    }

    public boolean containsVersion(Version version) {
        try {
            final VersionRange versionRange =
                    VersionRange.createFromVersionSpec(versionRequirement);
            final ArtifactVersion artifactVersion
                    = new DefaultArtifactVersion(version.toString());
            if (versionRange.getRecommendedVersion() != null) {
                return artifactVersion.equals(versionRange.getRecommendedVersion());
            }
            return versionRange.containsVersion(artifactVersion);
        } catch (InvalidVersionSpecificationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object obj) {
        return Util.equals(this, obj, (a, b) ->
                Objects.equals(a.versionRequirement, b.versionRequirement));
    }

    @Override
    public int hashCode() {
        return Objects.hash(versionRequirement);
    }

    @Override
    public String toString() {
        return versionRequirement;
    }

}
