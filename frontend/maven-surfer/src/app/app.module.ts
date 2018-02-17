import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";

import { ClarityModule } from "@clr/angular";

import { AppRoutingModule } from "./app-routing.module";

import { AppComponent } from './app.component';
import { PageNotFoundComponent } from "./page-not-found.component";

import { ProjectsModule } from "./projects/projects.module";
import { ImportModule } from "./import/import.module";
import { SettingsModule } from "./settings/settings.module";

@NgModule({
  declarations: [
    AppComponent,
    PageNotFoundComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    ClarityModule,
    ProjectsModule,
    ImportModule,
    SettingsModule,
    AppRoutingModule
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
