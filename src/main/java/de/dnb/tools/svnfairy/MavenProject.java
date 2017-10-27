package de.dnb.tools.svnfairy;

import java.util.Objects;
import java.util.Optional;

public class MavenProject {

    private final String file;
    private final MavenParentReference parent;
    private final MavenCoordinates coordinates;

    public MavenProject(String file,
                        MavenParentReference parent,
                        MavenCoordinates coordinates) {

        Objects.requireNonNull(file);
        Objects.requireNonNull(coordinates);

        this.file = file;
        this.parent = parent;
        this.coordinates = coordinates;
    }

    public String getFile() {
        return file;
    }

    public Optional<MavenParentReference> getParent() {
        return Optional.ofNullable(parent);
    }

    public MavenCoordinates getCoordinates() {
        return coordinates;
    }

}
