import { Component } from '@angular/core';

class ProjectImporterModel {

  groupId: string = "";
  artifactId: string = "";
  version: string = "";

  get gav(): string {
    let value = this.groupId;
    if (this.artifactId != '') {
      value += ':' + this.artifactId;
    } else if (this.version != '') {
      value += ':';
    }
    if (this.version != '') {
      value += ':' + this.version;
    }
    return value;
  }

  set gav(value: string) {
    const gav = value.split(':', 3);
    this.groupId = gav[0];
    this.artifactId = gav.length > 1 ? gav[1] : '';
    this.version = gav.length > 2 ? gav[2] : '';
  }
}

@Component({
  selector: 'app-single-project',
  templateUrl: './project-importer.component.html'
})
export class ProjectImporterComponent {

  model: ProjectImporterModel = new ProjectImporterModel();

  import() {
  }

}
