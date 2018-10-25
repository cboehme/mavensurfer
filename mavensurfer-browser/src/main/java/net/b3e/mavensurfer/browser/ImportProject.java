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
package net.b3e.mavensurfer.browser;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import net.b3e.mavensurfer.browser.db.JpaRepository;
import net.b3e.mavensurfer.browser.maven.ExtractInformation;
import net.b3e.mavensurfer.browser.model.ExtractionFailed;
import net.b3e.mavensurfer.browser.model.Gav;
import net.b3e.mavensurfer.browser.model.Outcome;
import net.b3e.mavensurfer.browser.model.PomFile;
import net.b3e.mavensurfer.browser.model.Project;

@RequestScoped
public class ImportProject {

    private ExtractInformation extractInformation;
    private JpaRepository jpaRepository;

    public ImportProject() {

        // Required by CDI
    }

    @Inject
    public ImportProject(ExtractInformation extractInformation,
                         JpaRepository jpaRepository) {

        this.extractInformation = extractInformation;
        this.jpaRepository = jpaRepository;
    }

    public void from(PomFile pomFile) {

        final Project project = extractInformation.fromPom(pomFile);
        if (project != null) {
            jpaRepository.create(project);
        }
    }

    public Outcome<Void, ExtractionFailed> with(Gav gav) {

        return extractInformation.fromProject(gav)
                .onSuccess(jpaRepository::create)
                .mapOutcome(r -> (Void) null, e -> e);
    }

}
