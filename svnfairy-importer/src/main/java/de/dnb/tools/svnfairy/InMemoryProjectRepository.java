package de.dnb.tools.svnfairy;

import static java.util.Objects.requireNonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class InMemoryProjectRepository implements ProjectRepository {

    private final Map<Gav, Project> projects = new HashMap<>();

    @Override
    public Optional<Project> findByGav(Gav gav) {
        return Optional.ofNullable(projects.get(gav));
    }

    @Override
    public void add(Project project) {
        requireNonNull(projects);
        projects.put(project.getGav(), project);
    }

}
