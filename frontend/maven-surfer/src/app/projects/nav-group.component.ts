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
import { Component, Input, OnInit } from '@angular/core';
import { Artifact } from "./artifact";
import { ProjectsService } from "./projects.service";

@Component({
  selector: 'app-nav-group',
  template: `
    <ng-container [clrLoading]="loading">
      <clr-tree-node *ngFor="let artifact of artifacts">
        <clr-icon shape="bundle"></clr-icon> {{artifact.artifactId}}
        <ng-template clrIfExpanded>
          <app-nav-project [artifactIdUrl]="artifact['@id']"></app-nav-project>
        </ng-template>
      </clr-tree-node>
    </ng-container>
  `
})
export class NavGroupComponent implements OnInit {

  @Input() groupIdUrl: string;

  loading: boolean;
  artifacts: Artifact[];

  constructor(private projectsService: ProjectsService) { }

  ngOnInit() {
    this.loading = true;
    this.projectsService.fetchArtifacts(this.groupIdUrl)
      .subscribe(artifacts => {
        this.artifacts = artifacts;
        this.loading = false;
      });
  }

}
