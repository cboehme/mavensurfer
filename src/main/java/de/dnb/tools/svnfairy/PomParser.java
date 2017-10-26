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

    private final XPathExpression groupIdPath;
    private final XPathExpression artifactIdPath;
    private final XPathExpression packagingPath;

    private Document pomDocument;

    public PomParser() throws XPathExpressionException {
        XPath xpath = XPathFactory.newInstance().newXPath();
        groupIdPath = xpath.compile("//project/groupId/text()");
        artifactIdPath = xpath.compile("//project/artifactId/text()");
        packagingPath = xpath.compile("//project/packaging/text()");
    }

    public MavenProject parsePom(String pomFile, byte[] pom)
            throws IOException, SAXException, ParserConfigurationException, XPathExpressionException {

        prepareDomOfPom(pom);
        MavenCoordinates coordinates = MavenCoordinates
                .withArtifactId(extractFromPom(artifactIdPath))
                .withGroupId(extractFromPom(groupIdPath))
                .withPackaging(extractFromPom(packagingPath))
                .create();
        return new MavenProject(pomFile, coordinates);
    }

    private void prepareDomOfPom(byte[] pom) throws ParserConfigurationException, IOException, SAXException {
        InputSource inputSource = new InputSource();
        inputSource.setByteStream(new ByteArrayInputStream(pom));
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setNamespaceAware(false);
        DocumentBuilder builder = domFactory.newDocumentBuilder();
        pomDocument = builder.parse(inputSource);
    }

    private String extractFromPom(XPathExpression itemPath) throws XPathExpressionException {
        NodeList nodes = (NodeList) itemPath.evaluate(pomDocument, XPathConstants.NODESET);
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < nodes.getLength(); i++) {
            text.append(nodes.item(i).getNodeValue());
        }
        return text.toString();
    }

}
