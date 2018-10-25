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
package net.b3e.mavensurfer.browser.db;

import java.time.ZonedDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import net.b3e.mavensurfer.browser.configuration.ConfigData;

@Entity
@Table(name = "configData")
class ConfigDataBean {

    @Id
    private String name;

    private ZonedDateTime lastModified;

    @Lob
    private byte[] data;

    @PrePersist
    @PreUpdate
    private void updateLastModified() {

        lastModified = ZonedDateTime.now();
    }

    static ConfigDataBean createFrom(ConfigData configData) {

        final ConfigDataBean bean = new ConfigDataBean();
        bean.name = configData.getName();
        bean.lastModified = configData.getLastModified().orElse(null);
        bean.data = configData.getData();
        return bean;
    }

    ConfigData toConfigData() {

        return new ConfigData(name, lastModified, data);
    }

}
