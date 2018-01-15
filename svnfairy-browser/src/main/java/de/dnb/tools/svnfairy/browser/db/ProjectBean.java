package de.dnb.tools.svnfairy.browser.db;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import de.dnb.tools.svnfairy.browser.model.Parent;

@Entity
@Table(name = "project",
       uniqueConstraints = @UniqueConstraint(columnNames = { "groupId", "artifactId", "version" }))
@NamedQueries({
    @NamedQuery(name = "Project.findByGav",
                query = "SELECT p FROM ProjectBean p WHERE p.groupId = :groupId AND p.artifactId = :artifactId AND p.version = :version"),
    @NamedQuery(name = "Project.findByDependency",
                query = "SELECT NEW de.dnb.tools.svnfairy.browser.db.ProjectWithDependency(d.owner, d) FROM DependencyBean d JOIN d.owner WHERE d.groupId = :groupId AND d.artifactId = :artifactId"),
    @NamedQuery(name = "Project.findByParent",
                query = "SELECT p FROM ProjectBean p WHERE p.parentGroupId = :parentGroupId AND p.parentArtifactId = :parentArtifactId"),
    @NamedQuery(name = "Project.findByGroupIdAndArtifactId",
                query = "SELECT p FROM ProjectBean p WHERE p.groupId = :groupId AND p.artifactId = :artifactId"),
    @NamedQuery(name = "Project.findGroupIds",
                query = "SELECT DISTINCT(p.groupId) FROM ProjectBean p"),
    @NamedQuery(name = "Project.findArtifactIds",
                query = "SELECT DISTINCT(p.artifactId) FROM ProjectBean p WHERE p.groupId = :groupId")
})
class ProjectBean {

    @Id
    @GeneratedValue
    Long id;

    String file;

    String groupId;
    String artifactId;
    String version;

    String packaging;

    String parentGroupId;
    String parentArtifactId;
    String parentVersionRange;

    void setParentCoordinates(Parent parent) {
        parentGroupId = parent.getGroupId().toString();
        parentArtifactId = parent.getArtifactId().toString();
        parentVersionRange = parent.getVersion().toString();
    }

}
