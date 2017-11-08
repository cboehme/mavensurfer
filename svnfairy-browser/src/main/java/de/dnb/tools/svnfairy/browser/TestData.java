package de.dnb.tools.svnfairy.browser;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.servlet.ServletContext;

import de.dnb.tools.svnfairy.ArtifactId;
import de.dnb.tools.svnfairy.GroupId;
import de.dnb.tools.svnfairy.Project;
import de.dnb.tools.svnfairy.Packaging;
import de.dnb.tools.svnfairy.Version;
import de.dnb.tools.svnfairy.browser.db.MavenProjectRepository;

@ApplicationScoped
public class TestData {

    @Inject
    private MavenProjectRepository repository;

    public void init(@Observes @Initialized(ApplicationScoped.class) ServletContext init) {
        populate();
    }

    private void populate() {
        Project project = new Project("test.xml");
        project.setGroupId(GroupId.of("org.metafacture"));
        project.setArtifactId(ArtifactId.of("metafacture-commons"));
        project.setVersion(Version.of("5.0.0-rc1"));
        project.setPackaging(Packaging.of("jar"));
        repository.create(project);
    }

}
