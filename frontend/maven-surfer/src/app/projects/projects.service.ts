/*
 * Copyright 2018 Christoph Böhme
 *
 * Licensed under the Apache License, Version 2.0 the "License";
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { Project } from './project'
import { Collection } from "./collection";
import { Group } from "./group";
import { Artifact } from "./artifact";
import { Dependant } from "./dependant";
import { Dependency } from "./dependency";

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

  findProjects(searchUrl: string) {
    return this.http.get<Collection<Project>>(this.baseUrl + "/" + searchUrl);
  }

  importProject(groupId: string, artifactId: string, version: string): Observable<any> {
    return this.http.post<any>(this.baseUrl + "/" + groupId + "/" + artifactId + "/" + version, null);
  }

}