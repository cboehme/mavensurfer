package de.dnb.tools.svnfairy.browser.db;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "pom",
       uniqueConstraints = @UniqueConstraint(columnNames = { "groupId", "artifactId", "version" }))
@NamedQuery(name = "Pom.findByGav",
            query = "SELECT pom FROM PomBean pom WHERE pom.groupId = :groupId AND pom.artifactId = :artifactId AND pom.version = :version")
class PomBean {

    @Id
    @GeneratedValue
    Long id;

    String file;

    String groupId;
    String artifactId;
    String version;

    String packaging;

}
