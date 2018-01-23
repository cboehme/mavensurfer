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

    private static final String mavenRepositoryProperty = "maven.repository";
    private static final String userHomeProperty = "user.home";

    private static final String defaultRepositoryLocation = ".m2/repository";

    public Path getDefaultLocalRepository() {

        final String localRepository = System.getProperty(mavenRepositoryProperty);
        if (localRepository != null) {
            return Paths.get(localRepository);
        }
        final Path userHome = Paths.get(System.getProperty(userHomeProperty));
        return userHome.resolve(defaultRepositoryLocation).toAbsolutePath();
    }

}
