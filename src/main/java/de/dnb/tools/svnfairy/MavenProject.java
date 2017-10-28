package de.dnb.tools.svnfairy;

import java.util.Optional;

public class MavenProject {

    private final String file;

    private MavenParent parent;

    private GroupId groupId;
    private ArtifactId artifactId;
    private Version version;
    private Packaging packaging;

    public MavenProject(String file) {
        this.file = file;
    }

    public String getFile() {
        return file;
    }

    public Optional<MavenParent> getParent() {
        return Optional.ofNullable(parent);
    }

    public void setParent(MavenParent parent) {
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

}
