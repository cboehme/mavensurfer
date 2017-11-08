package de.dnb.tools.svnfairy;

import java.util.Optional;

public interface ProjectRepository {

    Optional<Project> findByGav(Gav gav);

    void add(Project project);

}
