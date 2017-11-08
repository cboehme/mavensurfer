package de.dnb.tools.svnfairy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Project {

    private final String file;

    private Parent parent;

    private GroupId groupId;
    private ArtifactId artifactId;
    private Version version;
    private Packaging packaging;

    private List<Dependency> dependencies = new ArrayList<>();

    public Project(String file) {
        this.file = file;
    }

    public String getFile() {
        return file;
    }

    public Optional<Parent> getParent() {
        return Optional.ofNullable(parent);
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }

    public GroupId getGroupId() {
        return groupId;
    }

    public void setGroupId(GroupId groupId) {
        this.groupId = groupId;
    }

    public ArtifactId getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(ArtifactId artifactId) {
        this.artifactId = artifactId;
    }

    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    public Packaging getPackaging() {
        return packaging;
    }

    public void setPackaging(Packaging packaging) {
        this.packaging = packaging;
    }

    public List<Dependency> getDependencies() {
        return Collections.unmodifiableList(dependencies);
    }

    public void addDependency(Dependency dependency) {
        dependencies.add(dependency);
    }

}
