package de.dnb.tools.svnfairy;

import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.Optional;

public class ParentResolvingService {

    public void resolveProjects(Collection<Project> projects) {

        final Collection<Project> unresolvedProjects =
                findProjectsWithIncompleteCoordinates(projects);

        while (!unresolvedProjects.isEmpty()) {
            final int initialCount = unresolvedProjects.size();
            tryResolveProjects(projects, unresolvedProjects);
            if (initialCount == unresolvedProjects.size()) {
                // TODO: Add error handling
                return;
            }
        }
    }

    private void tryResolveProjects(Collection<Project> projects,
                                    Collection<Project> unresolvedProjects) {

        for (Project project : unresolvedProjects) {
            if (!project.getParent().isPresent()) {
                // TODO: Add error handling
                unresolvedProjects.remove(project);
            }
            Parent parent = project.getParent().get();
            Gav gav = Gav.of(parent.getGroupId(), parent.getArtifactId(),
                    parent.getVersion());
            Optional<Project> parentProject = findByGav(projects, gav);
            if (parentProject.isPresent()) {
                if (!project.getGroupId().isPresent()) {
                    project.setGroupId(parentProject.get().getGroupId().get());
                }
                if (!project.getVersion().isPresent()) {
                    project.setVersion(parentProject.get().getVersion().get());
                }
                unresolvedProjects.remove(project);
            }
        }
    }

    private Collection<Project> findProjectsWithIncompleteCoordinates(
            Collection<Project> projects) {

        return projects.stream()
                .filter(Project::hasIncompleteCoordinates)
                .collect(toList());
    }

    private Optional<Project> findByGav(Collection<Project> projects,
                                        Gav gav) {

        return projects.stream()
                .filter(p -> gav.equals(p.getGav()))
                .findFirst();
    }

}
