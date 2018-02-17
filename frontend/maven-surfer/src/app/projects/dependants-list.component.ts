import {Component, Input, OnChanges, SimpleChanges} from '@angular/core';
import {Dependant} from "./dependant";
import {ProjectsService} from "./projects.service";
import {isUndefined} from "util";

@Component({
  selector: 'app-dependants-list',
  template: `
    <article>
      <ul class="list-unstyled">
        <li *ngFor="let dependant of dependants">
          <a
            routerLink="/projects/{{dependant.project.groupId}}/{{dependant.project.artifactId}}/{{dependant.project.version}}">
            {{dependant.project.groupId}}:{{dependant.project.artifactId}}:{{dependant.type}}:{{dependant.classifier}}:{{dependant.project.version}}
          </a> (Scope: {{dependant.scope | lowercase}}{{dependant.optional ? ', optional' : ''}})
        </li>
      </ul>
    </article>
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
