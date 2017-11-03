package de.dnb.tools.svnfairy.browser;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named("pom")
@RequestScoped
public class PomBean {

    public String getArtifactId() {
        return "artifact-id";
    }

}
