package de.dnb.tools.svnfairy

import spock.lang.Specification

import java.nio.charset.StandardCharsets

class PomParserTest extends Specification {

    def xml
    def project

    PomParser pomParser = new PomParser()

    def "should not set parent if pom declares no parent" () {
        given:
        xml = """
        <project>
        </project>""".getBytes(StandardCharsets.UTF_8)

        when:
        project = pomParser.parsePom(new InMemoryPomFile("", xml))

        then:
        !project.parent.isPresent()
    }

    def "should set parent if pom declares parent" () {
        given:
        xml = """
            <project>
                <parent>
                    <groupId>de.dnb.tools</groupId>
                    <artifactId>test</artifactId>
                    <version>1.0</version>
                </parent>
            </project>""".getBytes(StandardCharsets.UTF_8)

        when:
        project = pomParser.parsePom(new InMemoryPomFile("", xml))

        then:
        project.parent.isPresent()
        with(project.parent.get()) {
            groupId.equals(GroupId.of("de.dnb.tools"))
            artifactId.equals(ArtifactId.of("test"))
            version.equals(Version.of("1.0"))
        }
    }

    def "should add dependency" () {
        given:
        xml = """
            <project>
                <dependencies>
                    <dependency>
                        <groupId>de.dnb.tools</groupId>
                        <artifactId>test</artifactId>
                        <version>1.0</version>
                        <classifier>classy</classifier>
                        <type>jar</type>
                        <scope>compile</scope>
                        <optional>false</optional>
                    </dependency>
                </dependencies>
            </project>""".getBytes(StandardCharsets.UTF_8)

        when:
        project = pomParser.parsePom(new InMemoryPomFile("", xml))

        then:
        project.dependencies.size() == 1
        with(project.dependencies.get(0)) {
            groupId.equals(GroupId.of("de.dnb.tools"))
            artifactId.equals(ArtifactId.of("test"))
            version.equals(VersionRequirement.of("1.0"))
            classifier.equals(Classifier.of("classy"))
            type.equals(Type.of("jar"))
            scope == Scope.COMPILE
            optional == false
        }
    }

}
