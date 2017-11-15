package de.dnb.tools.svnfairy.browser.db;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "dependency")
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
