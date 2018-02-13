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
import {Component, Input, OnChanges, OnInit} from '@angular/core';
import {ProjectsService} from "./projects.service";
import {Project} from "./project";
import {Dependency} from './dependency';
import {isUndefined} from "util";

@Component({
  selector: 'app-project-selector',
  template: `
    <a *ngFor="let project of projects" clrDropdownItem routerLink="/projects/{{project.groupId}}/{{project.artifactId}}/{{project.version}}">{{project.version}}</a>
  `,
  styles: []
})
export class ProjectSelectorComponent implements OnChanges {

  @Input() reference: Dependency;

  projects: Project[] = [];

  constructor(private projectsService: ProjectsService) { }

  ngOnChanges() {
    if (isUndefined(this.reference)) {
      this.projects = [];
    } else {
      this.projectsService.findProjects(this.reference.groupId + "/" + this.reference.artifactId + "?version-range=" + this.reference.versionRange)
        .subscribe(projects => this.projects = projects.member);
    }
  }

}
