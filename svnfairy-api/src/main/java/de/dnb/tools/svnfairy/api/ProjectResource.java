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
package de.dnb.tools.svnfairy.api;

import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import de.dnb.tools.svnfairy.api.datatypes.JsonCollection;
import de.dnb.tools.svnfairy.api.datatypes.JsonDependant;
import de.dnb.tools.svnfairy.api.datatypes.JsonDependency;
import de.dnb.tools.svnfairy.api.datatypes.JsonProject;

@Path("/projects/{groupId}/{artifactId}/{version}")
public interface ProjectResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    JsonProject getProject(@PathParam("groupId") @NotNull String groupId,
                           @PathParam("artifactId") @NotNull String artifactId,
                           @PathParam("version") @NotNull String version);

    @POST
    void indexProject(@PathParam("groupId") @NotNull String groupId,
                      @PathParam("artifactId") @NotNull String artifactId,
                      @PathParam("version") @NotNull String version);

    @GET
    @Path("/parents")
    @Produces(MediaType.APPLICATION_JSON)
    JsonCollection<JsonProject> getParents(@PathParam("groupId") @NotNull String groupId,
                                           @PathParam("artifactId") @NotNull String artifactId,
                                           @PathParam("version") @NotNull String version);

    @GET
    @Path("/dependencies")
    @Produces(MediaType.APPLICATION_JSON)
    JsonCollection<JsonDependency> getDependencies(@PathParam("groupId") @NotNull String groupId,
                                                   @PathParam("artifactId") @NotNull String artifactId,
                                                   @PathParam("version") @NotNull String version);

    @GET
    @Path("/children")
    @Produces(MediaType.APPLICATION_JSON)
    JsonCollection<JsonProject> getChildren(@PathParam("groupId") @NotNull String groupId,
                                            @PathParam("artifactId") @NotNull String artifactId,
                                            @PathParam("version") @NotNull String version);

    @GET
    @Path("/dependants")
    @Produces(MediaType.APPLICATION_JSON)
    JsonCollection<JsonDependant> getDependants(@PathParam("groupId") @NotNull String groupId,
                                                @PathParam("artifactId") @NotNull String artifactId,
                                                @PathParam("version") @NotNull String version);

}
