import {
  Component,
  Input,
  OnChanges,
  OnInit,
  SimpleChanges
} from '@angular/core';
import {Project} from './project';
import {ProjectsService} from "./projects.service";
import {isUndefined, log} from "util";

@Component({
  selector: 'app-project-list',
  template: `
    <h1>{{heading}}</h1>
    <ul>
      <li *ngFor="let project of projects">
        <a routerLink="/projects/{{project.groupId}}/{{project.artifactId}}/{{project.version}}">
          {{project.groupId}}:{{project.artifactId}}:{{project.version}}
        </a>
      </li>
    </ul>
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
      this.projectsService.getProjects(this.listUrl)
        .subscribe(projects => this.projects = projects.member);
    }
  }

}