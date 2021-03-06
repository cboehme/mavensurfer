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
package net.b3e.mavensurfer.browser.model;

import java.util.Objects;

import net.b3e.mavensurfer.browser.Util;

public final class GroupId {

    private final String groupId;

    private GroupId(String groupId) {
        this.groupId = groupId;
    }

    public static GroupId of(String groupId) {

        if (groupId == null) {
            return null;
        }
        return new GroupId(groupId);
    }

    @Override
    public boolean equals(Object obj) {

        return Util.equals(this, obj, (a, b) ->
                Objects.equals(a.groupId, b.groupId));
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId);
    }

    @Override
    public String toString() {
        return groupId;
    }

}
