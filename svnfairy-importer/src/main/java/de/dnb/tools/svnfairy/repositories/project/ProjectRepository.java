package de.dnb.tools.svnfairy.repositories.project;

import java.util.Optional;

import de.dnb.tools.svnfairy.model.Gav;
import de.dnb.tools.svnfairy.model.Project;

public interface ProjectRepository {

    Optional<Project> findByGav(Gav gav);

    void add(Project project);

}
