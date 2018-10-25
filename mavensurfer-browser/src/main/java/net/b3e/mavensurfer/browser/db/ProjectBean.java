/*
 * Copyright 2018 Christoph Böhme
 *
 * Licensed under the Apache License, Version 2.0 the "License";
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.b3e.mavensurfer.browser.db;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import net.b3e.mavensurfer.browser.model.Parent;

@Entity
@Table(name = "project",
       uniqueConstraints = @UniqueConstraint(columnNames = { "groupId", "artifactId", "version" }))
@NamedQueries({
    @NamedQuery(name = "Project.findByGav",
                query = "SELECT p FROM ProjectBean p WHERE p.groupId = :groupId AND p.artifactId = :artifactId AND p.version = :version"),
    @NamedQuery(name = "Project.findByDependency",
                query = "SELECT NEW net.b3e.mavensurfer.browser.db.ProjectWithDependency(d.owner, d) FROM DependencyBean d JOIN d.owner WHERE d.groupId = :groupId AND d.artifactId = :artifactId"),
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

    String name;
    String description;

    String parentGroupId;
    String parentArtifactId;
    String parentVersionRange;

    void setParentCoordinates(Parent parent) {
        parentGroupId = parent.getGroupId().toString();
        parentArtifactId = parent.getArtifactId().toString();
        parentVersionRange = parent.getVersionRange().toString();
    }

}
