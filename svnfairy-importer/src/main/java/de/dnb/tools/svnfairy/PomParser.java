package de.dnb.tools.svnfairy;

import static javax.xml.xpath.XPathConstants.NODESET;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import de.dnb.tools.svnfairy.collectors.PomFile;

public class PomParser {

    private final XPath xPath;

    private Document pomDocument;
    private Project project;

    public PomParser() {
        xPath = XPathFactory.newInstance().newXPath();
    }

    public Project parsePom(PomFile pomFile)
            throws IOException, SAXException, ParserConfigurationException, XPathExpressionException {

        prepareDomOfPom(pomFile.getContents());

        final Parent parent;
        if (has("//project/parent")) {
            parent = Parent.of(
                    GroupId.of(get("//project/parent/groupId/text()")),
                    ArtifactId.of(get("//project/parent/artifactId/text()")),
                    Version.of(get("//project/parent/version/text()")));
        } else {
            parent = null;
        }

        project = new Project(pomFile.getName());
        project.setParent(parent);
        project.setGroupId(GroupId.of(get("//project/groupId/text()")));
        project.setArtifactId(ArtifactId.of(get("//project/artifactId/text()")));
        project.setVersion(Version.of(get("//project/version/text()")));
        project.setPackaging(Packaging.of(get("//project/packaging/text()")));

        addDependencies();

        return project;
    }

    private void addDependencies() throws XPathExpressionException {
        NodeList dependencyElements = getNodes("//project/dependencies/*");
        for (int i = 0; i < dependencyElements.getLength(); i++) {
            Node dependencyElement = dependencyElements.item(i);
            Dependency dependency = new Dependency();
            dependency.setGroupId(GroupId.of(
                    get(dependencyElement, "//groupId/text()")));
            dependency.setArtifactId(ArtifactId.of(
                    get(dependencyElement, "//artifactId/text()")));
            dependency.setVersion(VersionRequirement.of(
                    get(dependencyElement, "//version/text()")));
            dependency.setClassifier(Classifier.of(
                    get(dependencyElement, "//classifier/text()")));
            dependency.setType(Type.of(
                    get(dependencyElement, "//type/text()")));
            dependency.setScope(Scope.valueOf(
                    get(dependencyElement, "//scope/text()").toUpperCase()));
            dependency.setOptional(Boolean.valueOf(
                    get(dependencyElement, "//optional/text()")));
            project.addDependency(dependency);
        }
    }

    private void prepareDomOfPom(byte[] pom) throws ParserConfigurationException, IOException, SAXException {
        InputSource inputSource = new InputSource();
        inputSource.setByteStream(new ByteArrayInputStream(pom));
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setNamespaceAware(false);
        DocumentBuilder builder = domFactory.newDocumentBuilder();
        pomDocument = builder.parse(inputSource);
    }

    private String get(Node contextNode, String path) throws XPathExpressionException {
        final XPathExpression compiledPath = xPath.compile(path);
        final NodeList nodes = (NodeList) compiledPath.evaluate(
                contextNode, NODESET);
        final StringBuilder text = new StringBuilder();
        for (int i = 0; i < nodes.getLength(); i++) {
            text.append(nodes.item(i).getNodeValue());
        }
        return text.toString();
    }

    private String get(String path) throws XPathExpressionException {
        return get(pomDocument, path);
    }

    private boolean has(String path) throws XPathExpressionException {
        final XPathExpression compiledPath = xPath.compile(path);
        final NodeList nodes = (NodeList) compiledPath.evaluate(
            pomDocument, NODESET);
        return nodes.getLength() > 0;
    }

    private NodeList getNodes(String path) throws XPathExpressionException {
        final XPathExpression compiledPath = xPath.compile(path);
        return (NodeList) compiledPath.evaluate(pomDocument, NODESET);
    }

}
