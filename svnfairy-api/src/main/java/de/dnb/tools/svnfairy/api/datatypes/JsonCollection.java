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
package de.dnb.tools.svnfairy.api.datatypes;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class JsonCollection<T> extends Referable {

    private long totalItems;
    private List<T> member;

    @XmlElement(name = "@type")
    public String getType() {
        return "JsonCollection";
    }

    public long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(long totalItems) {
        this.totalItems = totalItems;
    }

    public List<T> getMember() {
        return member;
    }

    public void setMember(List<T> member) {
        this.member = member;
    }

}
