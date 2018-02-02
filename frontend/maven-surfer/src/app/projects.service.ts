import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { Project } from './project'

@Injectable()
export class ProjectsService {

  private projectsUrl = 'http://localhost:8080/api/projects/de.dnb.tools.test.multiple-versions/project/1.0.0';

  constructor(private http: HttpClient) { }

  getProject(): Observable<Project> {
    return this.http.get<Project>(this.projectsUrl);
  }

}
