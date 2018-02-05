import { Component } from '@angular/core';

import { Project } from './project';
import { ProjectsService } from './projects.service';
import {Group} from "./group";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

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
