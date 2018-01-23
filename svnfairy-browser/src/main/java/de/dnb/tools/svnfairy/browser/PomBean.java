package de.dnb.tools.svnfairy.browser;

import static java.util.stream.Collectors.toList;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.dnb.tools.svnfairy.browser.db.JpaRepository;
import de.dnb.tools.svnfairy.browser.model.ArtifactId;
import de.dnb.tools.svnfairy.browser.model.GroupId;
import de.dnb.tools.svnfairy.browser.model.Project;
import de.dnb.tools.svnfairy.browser.model.Version;

@Named("pom")
@RequestScoped
public class PomBean {

    private static final Logger log = LoggerFactory.getLogger(PomBean.class);

    @Inject
    private JpaRepository repository;
    @Inject
    private GetChildren getChildren;
    @Inject
    private GetPossibleParents getPossibleParents;
    @Inject
    private GetDependents getDependents;

    private String groupId;
    private String artifactId;
    private String version;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getName() {
        return artifactId;
    }

    public String getGav() {
        return groupId + ":" + artifactId + ":" + version;
    }

    public String getFile() {
        Project project = repository.getByGav(GroupId.of(groupId),
                ArtifactId.of(artifactId), Version.of(version)).get();
        return project.getFile();
    }

    public List<String> getAllVersions() {
        List<Project> projects = repository.findProjectsWith(
                GroupId.of(groupId), ArtifactId.of(artifactId));
        return projects.stream()
                .map(p -> p.getVersion().toString())
                .collect(toList());
    }

    public List<Project> getPossibleParents() {
        Project project = repository.getByGav(GroupId.of(groupId),
                ArtifactId.of(artifactId), Version.of(version)).get();
        return getPossibleParents.of(project);
    }

    public List<Project> getChildren() {
        Project project = repository.getByGav(GroupId.of(groupId),
                ArtifactId.of(artifactId), Version.of(version)).get();
        return getChildren.of(project);
    }

    public List<Project> getDependents() {
        Project project = repository.getByGav(GroupId.of(groupId),
                ArtifactId.of(artifactId), Version.of(version)).get();
        return getDependents.of(project);
    }

}
