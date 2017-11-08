package de.dnb.tools.svnfairy;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

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
    public Collection<Project> findProjectsWithIncompleteCoordinates() {

        return projects.stream()
                .filter(Project::hasIncompleteCoordinates)
                .collect(toList());
    }

    @Override
    public void add(Project project) {
        requireNonNull(projects);

        projects.add(project);
    }

}
