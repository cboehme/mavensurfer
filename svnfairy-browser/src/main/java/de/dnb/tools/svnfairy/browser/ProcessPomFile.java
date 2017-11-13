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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.enterprise.context.RequestScoped;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

import de.dnb.tools.svnfairy.browser.model.PomFile;
import de.dnb.tools.svnfairy.browser.model.Project;

@RequestScoped
public class ProcessPomFile {

    private PomParser pomParser = new PomParser();
    private ParentResolvingService parentResolvingService =
            new ParentResolvingService();

    public void processPomFile(PomFile pomFile) throws SAXException,
            ParserConfigurationException, XPathExpressionException, IOException {

        final Collection<Project> projects = new ArrayList<>();
        Project project = pomParser.parsePom(pomFile);
        if (project != null) {
            projects.add(project);
        }

        parentResolvingService.resolveProjects(projects);
        projects.removeIf(Project::hasIncompleteCoordinates);
        projects.forEach(System.out::println);
    }

}
