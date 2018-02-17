import { Component } from '@angular/core';

import { ProjectsService } from "./projects.service";

import { Group } from "./group";

@Component({
  templateUrl: './projects.component.html'
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
