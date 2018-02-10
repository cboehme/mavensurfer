package de.dnb.tools.svnfairy.browser.db;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "dependency")
@NamedQueries(
        @NamedQuery(name = "Dependency.findByOwner",
                    query = "SELECT d FROM DependencyBean d WHERE d.owner = :owner")
)
public class DependencyBean {

    @Id
    @GeneratedValue
    Long id;

    @ManyToOne
    ProjectBean owner;

    String groupId;
    String artifactId;
    String version;
    String classifier;
    String type;
    String scope;
    boolean optional;

}
