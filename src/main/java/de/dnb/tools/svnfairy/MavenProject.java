package de.dnb.tools.svnfairy;

public class MavenProject {

    private final String file;
    private final MavenCoordinates coordinates;

    public MavenProject(String file, MavenCoordinates coordinates) {
        this.file = file;
        this.coordinates = coordinates;
    }

    public String getFile() {
        return file;
    }

    public MavenCoordinates getCoordinates() {
        return coordinates;
    }

}
