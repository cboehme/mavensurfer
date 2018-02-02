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

  project: Project = new Project();

  constructor(private projectsService: ProjectsService) { }

  ngOnInit() {
    this.getProject();
  }

  getProject(): void {
    this.projectsService.getProject()
      .subscribe(project => this.project = project);
  }

}
