import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { Project } from './project'
import {Collection} from "./collection";
import {Group} from "./group";
import {Artifact} from "./artifact";
import {Dependant} from "./dependant";
import {Dependency} from "./dependency";

@Injectable()
export class ProjectsService {

  private baseUrl = 'http://localhost:8080/api/projects';


  constructor(private http: HttpClient) { }

  getGroups(): Observable<Collection<Group>> {
    return this.http.get<Collection<Group>>(this.baseUrl);
  }

  getArtifacts(groupIdUrl: string): Observable<Collection<Artifact>> {
    return this.http.get<Collection<Artifact>>(groupIdUrl);

  }

  getProjects(artifactIdUrl: string): Observable<Collection<Project>> {
    return this.http.get<Collection<Project>>(artifactIdUrl);
  }

  getProject(groupdId: string, artifactId: string, version: string): Observable<Project> {
    return this.http.get<Project>(this.baseUrl + "/" + groupdId + "/" + artifactId + "/" + version);
  }

  getDependants(listUrl: string) {
    return this.http.get<Collection<Dependant>>(listUrl);
  }

  getDependencies(listUrl: string) {
    return this.http.get<Collection<Dependency>>(listUrl);
  }

}
