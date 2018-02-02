import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { Project } from './project'
import {Collection} from "./collection";

@Injectable()
export class ProjectsService {

  private projectsUrl = 'http://localhost:8080/api/projects/de.dnb.tools.test.multiple-versions/project';

  constructor(private http: HttpClient) { }

  getProjects(): Observable<Collection<Project>> {
    return this.http.get<Collection<Project>>(this.projectsUrl);
  }

}
