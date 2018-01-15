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
package de.dnb.tools.svnfairy.gateway;

import java.util.Base64;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import de.dnb.tools.svnfairy.api.Pom;
import de.dnb.tools.svnfairy.api.ProjectsResource;
import de.dnb.tools.svnfairy.model.PomFile;

public class PomFileProcessor {

    private final ProjectsResource projectsResource;
    private final Base64.Encoder base64Encoder = Base64.getEncoder();

    public PomFileProcessor() {

        final Client client = ClientBuilder.newClient();
        final WebTarget target = client.target("http://localhost:8080/api");
        projectsResource = ((ResteasyWebTarget) target).proxy(ProjectsResource.class);
    }

    public void sendToProcessingEngine(PomFile file) {

        final Pom pom = new Pom();
        pom.setName(file.getName());
        pom.setContents(encodeAsBase64(file.getContents()));

        projectsResource.indexPom(pom);
    }

    private String encodeAsBase64(byte[] data) {
        return base64Encoder.encodeToString(data);
    }

}
