import {Component, Input, OnInit} from '@angular/core';
import {ProjectReference} from "./project-reference";
import {Dependency} from "./dependency";
import {log} from "util";
import {Parent} from "./parent";

@Component({
  selector: 'app-project-reference',
  templateUrl: './project-reference.component.html',
  styles: []
})
export class ProjectReferenceComponent implements OnInit {

  @Input() reference: Dependency | Parent;

  constructor() { }

  ngOnInit() {
  }

  private isDependency(arg: any): arg is Dependency {
    return arg.classifier !== undefined;
  }

  asDependency(): Dependency {
    if (this.isDependency(this.reference)) {
      return this.reference as Dependency;
    }
    return null;
  }

}
