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
package net.b3e.mavensurfer.browser.configuration;

import java.util.Optional;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import net.b3e.mavensurfer.browser.db.ConfigDataRepository;

@RequestScoped
public class ManageSettings {

    private static final String settingsName = "global/settings.xml";

    @Inject
    private ConfigDataRepository repository;

    public void set(byte[] data) {

        repository.store(new ConfigData(settingsName, data));
    }

    public Optional<byte[]> get() {

        return repository.load(settingsName)
                .map(ConfigData::getData);
    }

    public boolean delete() {

        return repository.delete(settingsName);
    }

}
