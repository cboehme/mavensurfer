package de.dnb.tools.svnfairy.browser;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.servlet.ServletContext;

import de.dnb.tools.svnfairy.browser.db.JpaRepository;
import de.dnb.tools.svnfairy.browser.model.ArtifactId;
import de.dnb.tools.svnfairy.browser.model.GroupId;
import de.dnb.tools.svnfairy.browser.model.Packaging;
import de.dnb.tools.svnfairy.browser.model.Project;
import de.dnb.tools.svnfairy.browser.model.Version;

@ApplicationScoped
public class TestData {

    @Inject
    private JpaRepository repository;

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
