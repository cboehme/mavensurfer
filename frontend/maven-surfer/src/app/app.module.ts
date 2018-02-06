import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { HttpClientModule } from '@angular/common/http'

import { AppComponent } from './app.component';
import {ProjectsService} from './projects.service'
import {ClarityModule} from "@clr/angular";
import { NavGroupComponent } from './nav-group.component';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import { NavProjectComponent } from './nav-project.component';

@NgModule({
  declarations: [
    AppComponent,
    NavGroupComponent,
    NavProjectComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    ClarityModule,
    HttpClientModule
  ],
  providers: [
    ProjectsService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
