import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {HttpClientModule} from '@angular/common/http'

import {AppComponent} from './app.component';
import {AppRoutingModule} from "./app-routing.module";

import {ProjectsService} from './projects.service'
import {ClarityModule} from "@clr/angular";
import {NavGroupComponent} from './nav-group.component';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {NavProjectComponent} from './nav-project.component';
import {ProjectComponent} from './project.component';
import {DashboardComponent} from './dashboard.component';
import {ProjectListComponent} from './project-list.component';
import {DependantsListComponent} from './dependants-list.component';
import {DependenciesListComponent} from './dependencies-list.component';
import {ProjectReferenceComponent} from './project-reference.component';
import {ProjectSelectorComponent} from './project-selector.component';
import {SettingsModule} from "./settings/settings.module";

@NgModule({
  declarations: [
    AppComponent,
    NavGroupComponent,
    NavProjectComponent,
    ProjectComponent,
    DashboardComponent,
    ProjectListComponent,
    DependantsListComponent,
    DependenciesListComponent,
    ProjectReferenceComponent,
    ProjectSelectorComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    ClarityModule,
    HttpClientModule,
    SettingsModule,
    AppRoutingModule
  ],
  providers: [
    ProjectsService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
