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
import { of as observableOf } from 'rxjs/observable/of'
import { catchError } from "rxjs/operators";

import { ErrorService } from "../error.service";

import { Project } from './project'
import { Collection } from "./collection";
import { Group } from "./group";
import { Artifact } from "./artifact";
import { Dependant } from "./dependant";
import { Dependency } from "./dependency";

@Injectable()
export class ProjectsService {

  private baseUrl = 'http://localhost:8080/api/projects';

  constructor(private http: HttpClient,
              private errorService: ErrorService) { }

  fetchGroups(): Observable<Group[]> {
    return this.fetchCollection(this.baseUrl);
  }

  fetchArtifacts(groupIdUrl: string): Observable<Artifact[]> {
    return this.fetchCollection(groupIdUrl);
  }

  fetchProjects(artifactIdUrl: string): Observable<Project[]> {
    return this.fetchCollection(artifactIdUrl);
  }

  fetchProject(groupdId: string, artifactId: string, version: string): Observable<Project> {
    return this.http.get<Project>(this.baseUrl + "/" + groupdId + "/" + artifactId + "/" + version)
      .pipe(catchError(this.errorHandlerFor(null)));
  }

  getDependants(listUrl: string): Observable<Dependant[]> {
    return this.fetchCollection(listUrl);
  }

  getDependencies(listUrl: string): Observable<Dependency[]> {
    return this.fetchCollection(listUrl);
  }

  findProjects(searchUrl: string): Observable<Project[]> {
    return this.fetchCollection(this.baseUrl + "/" + searchUrl);
  }

  importProject(groupId: string, artifactId: string, version: string): Observable<any> {
    return this.http.post<any>(this.baseUrl + "/" + groupId + "/" + artifactId + "/" + version, null);
  }

  private fetchCollection<T>(url: string): Observable<T[]> {
    return this.http.get<Collection<T>>(url)
      .map(collection => collection.member)
      .pipe(catchError(this.errorHandlerFor([])));
  }

  private errorHandlerFor<T>(response: T) {
    return (error: any): Observable<T> => {
      this.errorService.raiseError(error);
      return observableOf(response);
    }
  }

}
