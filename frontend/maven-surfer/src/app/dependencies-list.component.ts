import {
  Component,
  Input,
  OnChanges,
  OnInit,
  SimpleChanges
} from '@angular/core';
import {ProjectsService} from "./projects.service";
import {Dependency} from "./dependency";
import {isNullOrUndefined} from "util";

@Component({
  selector: 'app-dependencies-list',
  templateUrl: './dependencies-list.component.html',
  styles: []
})
export class DependenciesListComponent implements OnChanges {

  @Input() listUrl: string;

  dependencies: Dependency[] = [];

  constructor(private projectsService: ProjectsService) { }

  ngOnChanges(changes: SimpleChanges): void {
    this.fetchDependencies();
  }

  private fetchDependencies() {
    if (isNullOrUndefined(this.listUrl)) {
      this.dependencies = [];
    } else {
      this.projectsService.getDependencies(this.listUrl)
        .subscribe(dependencies => this.dependencies = dependencies.member);
    }
  }

}
