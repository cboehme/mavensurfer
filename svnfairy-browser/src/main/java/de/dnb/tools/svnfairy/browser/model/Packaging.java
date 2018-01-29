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

import de.dnb.tools.svnfairy.browser.Util;

public final class Packaging {

    private final String packaging;

    private Packaging(String packaging) {
        this.packaging = packaging;
    }

    public static Packaging of(String packaging) {

        if (packaging == null) {
            return null;
        }
        return new Packaging(packaging);
    }

    @Override
    public boolean equals(Object obj) {

        return Util.equals(this, obj, (a, b) ->
                Objects.equals(a.packaging, b.packaging));
    }

    @Override
    public int hashCode() {
        return Objects.hash(packaging);
    }

    @Override
    public String toString() {
        return packaging;
    }

}
