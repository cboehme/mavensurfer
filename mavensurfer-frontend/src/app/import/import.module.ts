import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from "@angular/forms";

import { ClarityModule } from "@clr/angular";

import { ImportRoutingModule } from "./import-routing.module";

import { ImportComponent } from './import.component';
import { ProjectImporterComponent } from './project-importer.component';
import { RepositoryImporterComponent } from './repository-importer.component';

import { SubmissionService } from "./submission.service";

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    ClarityModule,
    ImportRoutingModule
  ],
  declarations: [
    ImportComponent,
    ProjectImporterComponent,
    RepositoryImporterComponent
  ],
  providers: [
    SubmissionService
  ]
})
export class ImportModule { }
