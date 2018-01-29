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

import java.io.IOException;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;

/**
 * Makes JAX-RS context objects available for CDI injection.
 * <p>
 * CDI does not support the {@code @Context} annotation of JAX-RS. Since JAX-RS
 * context objects can only be injected using the {@code @Context} annotation,
 * they are not injectable into CDI beans but only in JAX-RS annotated classes
 * (classes with a {@code @Path} or {@code @Provider} annotation).
 * <p>
 * This class bridges the gap between JAX-RD and CDI injection. It is a JAX-RS
 * annotated class and it provides CDI producers which return the context
 * objects that were injected into this class via JAX-RS.
 * <p>
 * The idea for this class is taken from a <a href=http://blog.christianbauer.name/Accessing%20request%20details%20with%20JAX-RS%20and%20CDI/">blog post by Christian Bauer</a>.
 */
@RequestScoped
@Provider
@PreMatching
public class JaxRsContextObjectsProducer implements ContainerRequestFilter {

    @Context
    private UriInfo uriInfo;

    @Produces
    public UriInfo getUriInfo() {

        return uriInfo;
    }


    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        // Nothing to do
    }

}
