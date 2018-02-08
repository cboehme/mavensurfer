import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { HttpClientModule } from '@angular/common/http'

import { AppComponent } from './app.component';
import {ProjectsService} from './projects.service'
import {ClarityModule} from "@clr/angular";
import { NavGroupComponent } from './nav-group.component';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import { NavProjectComponent } from './nav-project.component';
import {RouterModule} from "@angular/router";
import {appRoutes} from "./routes";
import { ProjectComponent } from './project.component';
import { DashboardComponent } from './dashboard.component';

@NgModule({
  declarations: [
    AppComponent,
    NavGroupComponent,
    NavProjectComponent,
    ProjectComponent,
    DashboardComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    RouterModule.forRoot(appRoutes, {enableTracing: true}),
    ClarityModule,
    HttpClientModule
  ],
  providers: [
    ProjectsService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
