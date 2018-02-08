import {Component, Input, OnInit} from '@angular/core';

import {Artifact} from "./artifact";
import {Project} from "./project";
import {ProjectsService} from './projects.service';

@Component({
  selector: 'app-nav-project',
  template: `
    <ng-container [clrLoading]="loading">
      <clr-tree-node *ngFor="let project of projects">
        <clr-icon shape="tag"></clr-icon>
        <a class="clr-treenode-link" 
           routerLink="projects/{{project.groupId}}/{{project.artifactId}}/{{project.version}}" 
           routerLinkActive="active">
          {{project.version}}
        </a>
      </clr-tree-node>
    </ng-container>
  `,
  inputs: ["artifactIdUrl"]
})
export class NavProjectComponent implements OnInit {

  @Input() artifactIdUrl: string;
  loading: boolean;
  projects: Project[];

  constructor(private projectsService: ProjectsService) { }

  ngOnInit() {
    this.loading = true;
    this.fetchProjects();
  }

  private fetchProjects() {
    this.projectsService.getProjects(this.artifactIdUrl)
      .subscribe(projects => {
        this.projects = projects.member;
        this.loading = false;
      });
  }

}
