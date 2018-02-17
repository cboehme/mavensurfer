import { Component } from '@angular/core';

import { ProjectsService } from "./projects.service";

import { Group } from "./group";

@Component({
  selector: 'app-projects',
  templateUrl: './projects.component.html',
  host: { 'class': 'content-container' }
})
export class ProjectsComponent {

  groups: Group[] = [];

  constructor(private projectsService: ProjectsService) { }

  ngOnInit() {
    this.getGroups();
  }

  getGroups(): void {
    this.projectsService.getGroups()
      .subscribe(groups => this.groups = groups.member);
  }

}
