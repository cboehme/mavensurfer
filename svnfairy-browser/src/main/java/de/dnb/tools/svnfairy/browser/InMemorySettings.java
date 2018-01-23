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
package de.dnb.tools.svnfairy.browser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.maven.settings.building.SettingsSource;

public class InMemorySettings implements SettingsSource {

    private final byte[] data;

    public InMemorySettings(byte[] data) {

        this.data = data;
    }

    @Override
    public InputStream getInputStream() throws IOException {

        return new ByteArrayInputStream(data);
    }

    @Override
    public String getLocation() {

        return "settings.xml";
    }

}
