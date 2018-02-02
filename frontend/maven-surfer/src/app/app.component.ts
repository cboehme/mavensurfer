import { Component } from '@angular/core';

import { Project } from './project';
import { ProjectsService } from './projects.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Welcome my app';

  projects: Project[] = [];

  constructor(private projectsService: ProjectsService) { }

  ngOnInit() {
    this.getProjects();
  }

  getProjects(): void {
    this.projectsService.getProjects()
      .subscribe(projects => this.projects = projects.member);
  }

}
