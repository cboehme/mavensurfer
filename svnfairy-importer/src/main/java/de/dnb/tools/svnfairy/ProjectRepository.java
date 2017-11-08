package de.dnb.tools.svnfairy;

import java.util.Collection;
import java.util.Optional;

public interface ProjectRepository {

    Optional<Project> findByGav(Gav gav);

    Collection<Project> findProjectsWithIncompleteCoordinates();

    void add(Project project);

}
