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
package de.dnb.tools.svnfairy.browser;

import java.nio.file.Path;
import java.nio.file.Paths;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Configuration {

    private final Path userHome = Paths.get(System.getProperty("user.home"));

    public Path getUserMavenSettings() {
        return userHome.resolve(".m2/settings.xml").toAbsolutePath();
    }

    public Path getGlobalMavenSettings() {
        return Paths.get("/etc/maven/settings.xml");
    }

    public Path getDefaultLocalRepository() {
        return userHome.resolve(".m2/repository").toAbsolutePath();
    }

}
