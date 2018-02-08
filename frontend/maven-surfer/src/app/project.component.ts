import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, ParamMap, Router} from "@angular/router";
import "rxjs/add/operator/switchMap";
import {Observable} from "rxjs/Observable";
import {Project} from "./project";
import {ProjectsService} from "./projects.service";
import {log} from "util";

@Component({
  selector: 'app-project',
  templateUrl: 'project.component.html',
  styles: []
})
export class ProjectComponent implements OnInit {

  project: Project = new Project();

  constructor(private route: ActivatedRoute,
              private router: Router,
              private projectsService: ProjectsService) { }

  ngOnInit() {
    this.route.paramMap.switchMap((params: ParamMap) => {
      return this.projectsService.getProject(params.get("groupId"),
        params.get("artifactId"), params.get("version"))
      })
      .subscribe(project => this.project = project);
  }

}
