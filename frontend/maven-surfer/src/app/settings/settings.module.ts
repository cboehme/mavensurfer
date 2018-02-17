import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BrowserModule } from "@angular/platform-browser";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from "@angular/common/http";

import { ClarityModule } from "@clr/angular";

import { SettingsRoutingModule } from './settings-routing.module';

import { SettingsComponent } from './settings.component';
import { SettingsEditorComponent } from './settings-editor.component';

import { ConfigurationService } from './configuration.service';

@NgModule({
  imports: [
    CommonModule,
    BrowserModule,
    BrowserAnimationsModule,
    ClarityModule,
    FormsModule,
    HttpClientModule,
    SettingsRoutingModule
  ],
  declarations: [
    SettingsComponent,
    SettingsEditorComponent,
  ],
  providers: [
    ConfigurationService
  ]
})
export class SettingsModule {
}
