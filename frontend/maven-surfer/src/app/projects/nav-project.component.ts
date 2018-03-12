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
import { Component, Input, OnInit } from '@angular/core';
import { Project } from "./project";
import { ProjectsService } from './projects.service';

@Component({
  selector: 'app-nav-project',
  template: `
    <ng-container [clrLoading]="loading">
      <clr-tree-node *ngFor="let project of projects">
        <clr-icon shape="tag"></clr-icon>
        <a class="clr-treenode-link" 
           routerLink="/projects/{{project.groupId}}/{{project.artifactId}}/{{project.version}}" 
           routerLinkActive="active">
          {{project.version}}
        </a>
      </clr-tree-node>
    </ng-container>
  `
})
export class NavProjectComponent implements OnInit {

  @Input() artifactIdUrl: string;

  loading: boolean;
  projects: Project[];

  constructor(private projectsService: ProjectsService) { }

  ngOnInit() {
    this.fetchProjects();
  }

  private fetchProjects() {
    this.loading = true;
    this.projectsService.fetchProjects(this.artifactIdUrl)
      .subscribe(
        projects => {
          this.projects = projects;
          this.loading = false;
        });
  }

}
