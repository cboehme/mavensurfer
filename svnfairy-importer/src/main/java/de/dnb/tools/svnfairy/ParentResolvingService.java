package de.dnb.tools.svnfairy;

import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.dnb.tools.svnfairy.model.Gav;
import de.dnb.tools.svnfairy.model.Parent;
import de.dnb.tools.svnfairy.model.Project;

public class ParentResolvingService {

    private static final Logger log =
            LoggerFactory.getLogger(ParentResolvingService.class);

    public void resolveProjects(Collection<Project> projects) {

        final Collection<Project> unresolvedProjects =
                findProjectsWithIncompleteCoordinates(projects);

        while (!unresolvedProjects.isEmpty()) {
            final int initialCount = unresolvedProjects.size();
            tryResolveProjects(projects, unresolvedProjects);
            if (initialCount == unresolvedProjects.size()) {
                log.warn("Could not resolve all projects:");
                unresolvedProjects.forEach(p -> log.info(p.toString()));
                return;
            }
        }
    }

    private void tryResolveProjects(Collection<Project> projects,
                                    Collection<Project> unresolvedProjects) {

        Iterator<Project> iter = unresolvedProjects.iterator();
        while (iter.hasNext()) {
            Project project = iter.next();
            if (!project.getParent().isPresent()) {
                log.warn("Project has incomplete coordinates but defines no parent: {}",
                        project);
                iter.remove();
                continue;
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
                iter.remove();
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
                .filter(p -> gav.equals(p.getGav().orElse(null)))
                .findFirst();
    }

}
