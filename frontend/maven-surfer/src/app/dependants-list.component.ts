import {Component, Input, OnChanges, SimpleChanges} from '@angular/core';
import {Dependant} from "./dependant";
import {ProjectsService} from "./projects.service";
import {isUndefined} from "util";

@Component({
  selector: 'app-dependants-list',
  template: `
    <h1>{{heading}}</h1>
    <ul>
      <li *ngFor="let dependant of dependants">
        <a routerLink="/projects/{{dependant.project.groupId}}/{{dependant.project.artifactId}}/{{dependant.project.version}}">
          {{dependant.project.groupId}}:{{dependant.project.artifactId}}:{{dependant.project.version}}
        </a><br>
        <span style="margin-left: 2em;">Classifier: {{dependant.classifier || 'None'}}</span>
        <span style="margin-left: 1em;">Type: {{dependant.type || 'None'}}</span>
        <span style="margin-left: 1em;">Scope: {{dependant.scope | titlecase}}</span>
        <span style="margin-left: 1em;">{{dependant.optional ? 'Optional' : ''}}</span>
      </li>
    </ul>
  `,
  styles: []
})
export class DependantsListComponent implements OnChanges {

  @Input() heading: string;
  @Input() listUrl: string;

  dependants: Dependant[] = [];

  constructor(private projectsService: ProjectsService) { }

  ngOnChanges(changes: SimpleChanges): void {
    this.fetchDependants();
  }

  private fetchDependants() {
    if (isUndefined(this.listUrl)) {
      this.dependants = [];
    } else {
      this.projectsService.getDependants(this.listUrl)
        .subscribe(dependants => this.dependants = dependants.member);
    }
  }

}