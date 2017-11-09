package de.dnb.tools.svnfairy;

import java.util.Collection;
import java.util.Optional;

public class ParentResolvingService {

    public void resolveProjects(Collection<Project> projects) {

        Collection<Project> unresolvedProjects =
                projectRepository.findProjectsWithIncompleteCoordinates();

        while (!unresolvedProjects.isEmpty()) {
            for (Project project : unresolvedProjects) {
                if (!project.getParent().isPresent()) {
                    // TODO: Add error handling
                    unresolvedProjects.remove(project);
                }
                Parent parent = project.getParent().get();
                Gav gav = Gav.of(parent.getGroupId(), parent.getArtifactId(),
                        parent.getVersion());
                Optional<Project> parentProject =
                        projectRepository.findByGav(gav);
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
    }

}
