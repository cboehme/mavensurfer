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
package de.dnb.tools.svnfairy.batchindexer;

import java.util.Properties;

import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.core.Response;

import de.dnb.tools.svnfairy.api.IndexingJobsResource;
import de.dnb.tools.svnfairy.api.JobSubmission;

@RequestScoped
public class IndexingJobResourceImpl implements IndexingJobsResource {

    @Override
    public Response submitJob(JobSubmission jobSubmission) {

        final IndexingJob indexingJob = new IndexingJob(jobSubmission.getType(),
                jobSubmission.getSettings());

        final JobOperator jobOperator = BatchRuntime.getJobOperator();
        final Properties properties = new Properties();
        properties.putAll(jobSubmission.getSettings());
        jobOperator.start(jobSubmission.getType(), properties);

        return Response.accepted().build();
    }

}
