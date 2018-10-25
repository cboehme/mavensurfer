import { Component } from '@angular/core';
import { SubmissionService } from "./submission.service";

class RepositoryImporterModel {

  nexusUrl: string = "";
  repositoryId: string = "";
  basePath: string = "";

}
@Component({
  selector: 'app-repository-importer',
  templateUrl: './repository-importer.component.html'
})
export class RepositoryImporterComponent {

  model: RepositoryImporterModel = new RepositoryImporterModel();
  importError: string;
  importOk: boolean;

  constructor(private submissionService: SubmissionService) { }

  import() {
    this.importError = null;
    this.importOk = false;
    this.submissionService.submitNexusJob(this.model.nexusUrl,
      this.model.repositoryId, this.model.basePath)
      .subscribe(
        () => this.importOk = true,
        () => this.importError = "Job submission failed"
        );
  }

}
