import {Component, Input, OnInit} from '@angular/core';
import {Project} from './project';
import {ProjectsService} from "./projects.service";
import {log} from "util";

@Component({
  selector: 'app-project-list',
  template: `
    <h1>{{heading}}</h1>
    <ul>
      <li *ngFor="let project of projects">
        {{project.groupId}}:{{project.artifactId}}:{{project.version}}
      </li>
    </ul>
  `,
  styles: [],
  inputs: ["heading", "projectListUrl"]
})
export class ProjectListComponent {

  @Input() heading: string;
  @Input() projectListUrl: string;
  private projectListUrlOld: string = "";
  projects: Project[] = [];

  constructor(private projectsService: ProjectsService) { }

  ngDoCheck() {
    if (this.projectListUrlOld != this.projectListUrl) {
      log("fetchProjects URL: " + this.projectListUrl);
      this.fetchProjects();
      this.projectListUrlOld = this.projectListUrl;
    }
  }

  private fetchProjects() {
    this.projectsService.getProjects(this.projectListUrl)
      .subscribe(projects => this.projects = projects.member);
  }

}
