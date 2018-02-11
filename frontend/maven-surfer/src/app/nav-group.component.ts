import {Component, Input, OnInit} from '@angular/core';
import {Artifact} from "./artifact";
import {ProjectsService} from "./projects.service";

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
    this.fetchArtifacts();
  }

  private fetchArtifacts() {
    this.projectsService.getArtifacts(this.groupIdUrl)
      .subscribe(artifacts => {
        this.artifacts = artifacts.member;
        this.loading = false;
      });
  }

}