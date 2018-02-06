import {Component, Input, OnInit} from '@angular/core';
import {Artifact} from "./artifact";
import {ProjectsService} from "./projects.service";
import {log} from "util";

@Component({
  selector: 'app-nav-group',
  template: `
    <ng-container [clrLoading]="loading">
      <clr-tree-node *ngFor="let artifact of artifacts">
        {{artifact.artifactId}}
      </clr-tree-node>
    </ng-container>
  `,
  inputs: ["groupIdUrl"]
})
export class NavGroupComponent implements OnInit {

  @Input() groupIdUrl: string;
  loading: boolean;
  artifacts: Artifact[];

  constructor(private projectsService: ProjectsService) { }

  ngOnInit() {
    log("GroupIdUrl: " + this.groupIdUrl);
    this.loading = true;
    this.fetchArtifacts();
    this.loading = false;
  }

  private fetchArtifacts() {
    this.projectsService.getArtifacts(this.groupIdUrl)
      .subscribe(artifacts => this.artifacts = artifacts.member);
  }

}
