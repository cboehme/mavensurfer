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

import static java.util.Objects.requireNonNull;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.dnb.tools.svnfairy.api.Pom;
import de.dnb.tools.svnfairy.api.PomResource;
import de.dnb.tools.svnfairy.browser.model.PomFile;

@RequestScoped
public class PomResourceImpl implements PomResource {

    private static final Logger log = LoggerFactory.getLogger(
            PomResourceImpl.class);

    @Inject
    private ProcessPomFile processPomFile;

    private final Base64.Decoder base64Decoder = Base64.getDecoder();

    @Override
    public Response importPom(Pom pom) {

        requireNonNull(pom);

        final byte[] contents = base64Decoder.decode(pom.getContents());
        final PomFile pomFile = new PomFile(pom.getName(), contents);

        log.info("Received {}", pomFile.getName());
        log.debug("POM: {}", new String(pomFile.getContents(), StandardCharsets.UTF_8));

        processPomFile.processPomFile(pomFile);

        return Response.ok().build();
    }

}
