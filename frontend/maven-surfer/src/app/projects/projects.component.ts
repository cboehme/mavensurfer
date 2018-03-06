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

  constructor(private projectsService: ProjectsService) {

    this.projectsService.groups$
      .subscribe(groups => this.groups = groups.member);
  }

  ngOnInit() {
    this.projectsService.fetchGroups();
  }

}
