import {Component, Input, OnInit} from '@angular/core';
import {Dependency} from "./dependency";
import {log} from "util";

@Component({
  selector: 'app-project-reference',
  templateUrl: './project-reference.component.html',
  styles: []
})
export class ProjectReferenceComponent implements OnInit {

  @Input() reference: Dependency;

  constructor() { }

  ngOnInit() {
  }

  resolve() {
    log("Resolve!");
  }

}
