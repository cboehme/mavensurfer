import {Component, Input, OnChanges, SimpleChanges} from '@angular/core';
import {Project} from './project';
import {ProjectsService} from "./projects.service";
import {isUndefined} from "util";

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
      this.projectsService.getProjects(this.listUrl)
        .subscribe(projects => this.projects = projects.member);
    }
  }

}
