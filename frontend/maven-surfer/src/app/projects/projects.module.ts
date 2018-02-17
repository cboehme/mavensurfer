import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BrowserModule } from "@angular/platform-browser";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { HttpClientModule } from "@angular/common/http";

import { ClarityModule } from "@clr/angular";

import { ProjectsRoutingModule } from "./projects-routing.module";

import { ProjectsComponent } from './projects.component';
import { NavGroupComponent } from "./nav-group.component";
import { NavProjectComponent } from "./nav-project.component";
import { DashboardComponent } from './dashboard.component';
import { ProjectComponent } from "./project.component";
import { ProjectListComponent } from "./project-list.component";
import { ProjectSelectorComponent } from "./project-selector.component";
import { ProjectReferenceComponent } from "./project-reference.component";
import { DependantsListComponent } from "./dependants-list.component";
import { DependenciesListComponent } from "./dependencies-list.component";

import { ProjectsService } from "./projects.service";

@NgModule({
  imports: [
    CommonModule,
    BrowserModule,
    BrowserAnimationsModule,
    ClarityModule,
    HttpClientModule,
    ProjectsRoutingModule
  ],
  declarations: [
    ProjectsComponent,
    NavGroupComponent,
    NavProjectComponent,
    DashboardComponent,
    ProjectComponent,
    ProjectListComponent,
    ProjectReferenceComponent,
    ProjectSelectorComponent,
    DependantsListComponent,
    DependenciesListComponent
  ],
  providers: [
    ProjectsService
  ]
})
export class ProjectsModule {
}
