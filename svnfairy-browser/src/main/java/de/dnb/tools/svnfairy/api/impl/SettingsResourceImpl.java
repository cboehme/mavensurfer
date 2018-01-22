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
package de.dnb.tools.svnfairy.api.impl;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.dnb.tools.svnfairy.api.SettingsResource;

@Path("/")
@RequestScoped
public class SettingsResourceImpl implements SettingsResource {

    private static final Logger log =
            LoggerFactory.getLogger(SettingsResourceImpl.class);

    @Override
    public Response getSettings() {

        return Response.ok().build();
    }

    @Override
    public Response putSettings(byte[] data) {

        return Response.ok().build();
    }

    @Override
    public Response deleteSettings() {

        return Response.ok().build();
    }

}
