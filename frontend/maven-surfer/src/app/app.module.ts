import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { HttpClientModule } from "@angular/common/http";

import { ClarityModule } from "@clr/angular";

import { ProjectsModule } from "./projects/projects.module";
import { ImportModule } from "./import/import.module";
import { SettingsModule } from "./settings/settings.module";

import { AppRoutingModule } from "./app-routing.module";

import { AppComponent } from './app.component';
import { PageNotFoundComponent } from "./page-not-found.component";

import { ErrorService } from "./error.service";


@NgModule({
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    ClarityModule,
    ProjectsModule,
    ImportModule,
    SettingsModule,
    AppRoutingModule
  ],
  declarations: [
    AppComponent,
    PageNotFoundComponent
  ],
  providers: [
    ErrorService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
