package de.dnb.tools.svnfairy;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import de.dnb.tools.svnfairy.model.Gav;
import de.dnb.tools.svnfairy.model.Project;

public class InMemoryProjectRepository implements ProjectRepository {

    private final Collection<Project> projects = new ArrayList<>();

    @Override
    public Optional<Project> findByGav(Gav gav) {
        requireNonNull(gav);

        return projects.stream()
                .filter(p -> gav.equals(p.getGav()))
                .findFirst();
    }

    @Override
    public void add(Project project) {
        requireNonNull(projects);

        projects.add(project);
    }

}
