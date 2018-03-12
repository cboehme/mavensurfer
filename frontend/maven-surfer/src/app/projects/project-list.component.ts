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
import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { Project } from './project';
import { ProjectsService } from "./projects.service";
import { isUndefined } from "util";

@Component({
  selector: 'app-project-list',
  template: `
    <article>
      <ul class="list-unstyled">
        <li *ngFor="let project of projects">
          <a
            routerLink="/projects/{{project.groupId}}/{{project.artifactId}}/{{project.version}}">
            {{project.groupId}}:{{project.artifactId}}:{{project.version}}
          </a>
        </li>
      </ul>
    </article>
  `,
  styles: []
})
export class ProjectListComponent implements OnChanges {

  @Input() heading: string;
  @Input() listUrl: string;

  projects: Project[] = [];

  constructor(private projectsService: ProjectsService) { }

  ngOnChanges(changes: SimpleChanges) {
      this.fetchProjects();
  }

  private fetchProjects() {
    if (isUndefined(this.listUrl)) {
      this.projects = [];
    } else {
      this.projectsService.fetchProjects(this.listUrl)
        .subscribe(projects => this.projects = projects);
    }
  }

}
