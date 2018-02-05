import { Component, OnInit, Input } from '@angular/core';
import {Artifact} from "./artifact";
import {ProjectsService} from "./projects.service";

@Component({
  selector: 'app-nav-group',
  template: `
    <ng-container [clrLoading]="loading">
      <clr-tree-node *ngFor="let artifact of artifacts">
        {{artifact.artifactId}}
      </clr-tree-node>
    </ng-container>
  `
})
export class NavGroupComponent implements OnInit {

  @Input() node: string;
  loading: boolean;
  artifacts: Artifact[];

  constructor(private projectsService: ProjectsService) { }

  ngOnInit() {
    this.loading = true;
    this.fetchArtifacts();
    this.loading = false;
  }

  private fetchArtifacts() {
    this.projectsService.getArtifacts()
      .subscribe(artifacts => this.artifacts = artifacts.member);
  }

}
