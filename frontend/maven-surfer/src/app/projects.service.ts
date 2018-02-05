import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { Project } from './project'
import {Collection} from "./collection";
import {Group} from "./group";
import {Artifact} from "./artifact";

@Injectable()
export class ProjectsService {

  private baseUrl = 'http://localhost:8080/api/projects';
  private projectsUrl = 'http://localhost:8080/api/projects/de.dnb.tools.test.multiple-versions/project';

  constructor(private http: HttpClient) { }

  getGroups(): Observable<Collection<Group>> {
    return this.http.get<Collection<Group>>(this.baseUrl);
  }

  getArtifacts(): Observable<Collection<Artifact>> {
    return this.http.get<Collection<Artifact>>(this.baseUrl + "/de.dnb.tools.test.import-scope");

  }

  getProjects(): Observable<Collection<Project>> {
    return this.http.get<Collection<Project>>(this.projectsUrl);
  }
}
