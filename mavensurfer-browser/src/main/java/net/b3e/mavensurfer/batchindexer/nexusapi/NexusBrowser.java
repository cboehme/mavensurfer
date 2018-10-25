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
package net.b3e.mavensurfer.batchindexer.nexusapi;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

public interface NexusBrowser {

    @GET
    @Path("/service/local/repositories/{repositoryId}/content/{path:.*}")
    @Produces(MediaType.APPLICATION_XML)
    Content browse(@PathParam("repositoryId") String repositoryId,
                   @PathParam("path") String path);

    @GET
    @Path("/service/local/repositories/{repositoryId}/content/{path:.*}")
    byte[] getContent(@PathParam("repositoryId") String repositoryId,
                      @PathParam("path") String path);

}
