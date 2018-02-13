import {Component, Input, OnInit} from '@angular/core';
import {Dependency} from "./dependency";

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

}
