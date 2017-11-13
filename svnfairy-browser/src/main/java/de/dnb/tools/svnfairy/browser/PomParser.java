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

import static javax.xml.xpath.XPathConstants.NODESET;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import de.dnb.tools.svnfairy.browser.model.ArtifactId;
import de.dnb.tools.svnfairy.browser.model.Classifier;
import de.dnb.tools.svnfairy.browser.model.Dependency;
import de.dnb.tools.svnfairy.browser.model.GroupId;
import de.dnb.tools.svnfairy.browser.model.Packaging;
import de.dnb.tools.svnfairy.browser.model.Parent;
import de.dnb.tools.svnfairy.browser.model.PomFile;
import de.dnb.tools.svnfairy.browser.model.Project;
import de.dnb.tools.svnfairy.browser.model.Scope;
import de.dnb.tools.svnfairy.browser.model.Type;
import de.dnb.tools.svnfairy.browser.model.Version;
import de.dnb.tools.svnfairy.browser.model.VersionRequirement;

public class PomParser {

    private static final Logger log = LoggerFactory.getLogger(PomParser.class);

    private final XPath xPath;

    private Document pomDocument;
    private Project project;

    public PomParser() {
        xPath = XPathFactory.newInstance().newXPath();
    }

    public Project parsePom(PomFile pomFile) throws IOException, SAXException,
            ParserConfigurationException, XPathExpressionException {

        try {
            doParsePom(pomFile);
        } catch (Exception e) {
            log.error("Failed to parse pom {}", pomFile, e);
            return null;
        }

        return project;
    }

    private void doParsePom(
            PomFile pomFile) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
        prepareDomOfPom(pomFile.getContents());

        final Parent parent;
        if (has("//project/parent")) {
            parent = Parent.of(
                    GroupId.of(getText("//project/parent/groupId")),
                    ArtifactId.of(getText("//project/parent/artifactId")),
                    Version.of(getText("//project/parent/version")));
        } else {
            parent = null;
        }

        project = new Project(pomFile.getName());
        project.setParent(parent);
        project.setGroupId(GroupId.of(getText("//project/groupId")));
        project.setArtifactId(ArtifactId.of(getText("//project/artifactId")));
        project.setVersion(Version.of(getText("//project/version")));
        project.setPackaging(Packaging.of(getText("//project/packaging")));

        addDependencies();
    }

    private void addDependencies() throws XPathExpressionException {
        NodeList dependencyElements = getNodes("//project/dependencies/*");
        for (int i = 0; i < dependencyElements.getLength(); i++) {
            Node dependencyElement = dependencyElements.item(i);
            Dependency dependency = new Dependency();
            dependency.setGroupId(GroupId.of(
                    getText(dependencyElement, "groupId")));
            dependency.setArtifactId(ArtifactId.of(
                    getText(dependencyElement, "artifactId")));
            dependency.setVersion(VersionRequirement.of(
                    getText(dependencyElement, "version")));
            dependency.setClassifier(Classifier.of(
                    getText(dependencyElement, "classifier")));
            dependency.setType(Type.of(
                    getText(dependencyElement, "type")));
            if (has(dependencyElement, "scope")) {
                dependency.setScope(Scope.valueOf(
                        getText(dependencyElement, "scope").toUpperCase()));
            }
            dependency.setOptional(Boolean.valueOf(
                    getText(dependencyElement, "optional")));
            project.addDependency(dependency);
        }
    }

    private void prepareDomOfPom(byte[] pom)
            throws ParserConfigurationException, IOException, SAXException {

        InputSource inputSource = new InputSource();
        inputSource.setByteStream(new ByteArrayInputStream(pom));
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setNamespaceAware(false);
        DocumentBuilder builder = domFactory.newDocumentBuilder();
        pomDocument = builder.parse(inputSource);
    }

    private String getText(Node contextNode, String path)
            throws XPathExpressionException {

        if (!has(contextNode, path)) {
            return null;
        }
        final XPathExpression compiledPath = xPath.compile(path + "/text()");
        final NodeList nodes = (NodeList) compiledPath.evaluate(
                contextNode, NODESET);
        final StringBuilder text = new StringBuilder();
        for (int i = 0; i < nodes.getLength(); i++) {
            text.append(nodes.item(i).getNodeValue());
        }
        return text.toString();
    }

    private String getText(String path) throws XPathExpressionException {
        return getText(pomDocument, path);
    }

    private boolean has(Node contextNode, String path)
            throws XPathExpressionException {

        final XPathExpression compiledPath = xPath.compile(path);
        final NodeList nodes = (NodeList) compiledPath.evaluate(
                contextNode, NODESET);
        return nodes.getLength() > 0;
    }

    private boolean has(String path) throws XPathExpressionException {
        return has(pomDocument, path);
    }

    private NodeList getNodes(String path) throws XPathExpressionException {
        final XPathExpression compiledPath = xPath.compile(path);
        return (NodeList) compiledPath.evaluate(pomDocument, NODESET);
    }

}
