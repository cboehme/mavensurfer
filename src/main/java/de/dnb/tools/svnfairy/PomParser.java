package de.dnb.tools.svnfairy;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class PomParser {

    private final XPath xPath;

    private Document pomDocument;

    public PomParser() {
        xPath = XPathFactory.newInstance().newXPath();
    }

    public MavenProject parsePom(PomFile pomFile)
            throws IOException, SAXException, ParserConfigurationException, XPathExpressionException {

        prepareDomOfPom(pomFile.getContents());

        final MavenParent parent;
        if (has("//project/parent")) {
            parent = MavenParent.of(
                    GroupId.of(get("//project/parent/groupId/text()")),
                    ArtifactId.of(get("//project/parent/artifactId/text()")),
                    Version.of(get("//project/parent/version/text()")));
        } else {
            parent = null;
        }

        final MavenProject project = new MavenProject(pomFile.getName());
        project.setParent(parent);
        project.setGroupId(GroupId.of(get("//project/groupId/text()")));
        project.setArtifactId(ArtifactId.of(get("//project/artifactId/text()")));
        project.setVersion(Version.of(get("//project/version/text()")));
        project.setPackaging(Packaging.of(get("//project/packaging/text()")));

        return project;
    }

    private void prepareDomOfPom(byte[] pom) throws ParserConfigurationException, IOException, SAXException {
        InputSource inputSource = new InputSource();
        inputSource.setByteStream(new ByteArrayInputStream(pom));
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setNamespaceAware(false);
        DocumentBuilder builder = domFactory.newDocumentBuilder();
        pomDocument = builder.parse(inputSource);
    }

    private String get(String path) throws XPathExpressionException {
        final XPathExpression compiledPath = xPath.compile(path);
        final NodeList nodes = (NodeList) compiledPath.evaluate(
            pomDocument, XPathConstants.NODESET);
        final StringBuilder text = new StringBuilder();
        for (int i = 0; i < nodes.getLength(); i++) {
            text.append(nodes.item(i).getNodeValue());
        }
        return text.toString();
    }

    private boolean has(String path) throws XPathExpressionException {
        final XPathExpression compiledPath = xPath.compile(path);
        final NodeList nodes = (NodeList) compiledPath.evaluate(
            pomDocument, XPathConstants.NODESET);
        return nodes.getLength() > 0;
    }

}
