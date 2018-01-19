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
package de.dnb.tools.svnfairy.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/projects")
public interface ProjectsResource {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    Response indexPom(Pom pom);

    @POST
    @Path("/{groupId}/{artifactId}/{version}")
    Response indexGav(@PathParam("groupId") String groupId,
                      @PathParam("artifactId") String artifactId,
                      @PathParam("version") String version);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    Response listGroupIds();

    @GET
    @Path("/{groupId}")
    @Produces(MediaType.APPLICATION_JSON)
    Response listArtifactIdsFor(@PathParam("groupId") String groupId);

    @GET
    @Path("/{groupId}/{artifactId}")
    @Produces(MediaType.APPLICATION_JSON)
    Response listVersionsFor(@PathParam("groupId") String groupId,
                             @PathParam("artifactId") String artifactId);

}
