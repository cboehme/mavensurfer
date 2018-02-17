import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {BrowserModule} from "@angular/platform-browser";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {RouterModule} from "@angular/router";

import {ClarityModule} from "@clr/angular";

import {SettingsComponent} from './settings.component';
import {SettingsEditorComponent} from './settings-editor.component';
import {ConfigurationService} from './configuration.service';

@NgModule({
  imports: [
    CommonModule,
    BrowserModule,
    FormsModule,
    RouterModule,
    BrowserAnimationsModule,
    ClarityModule
  ],
  declarations: [SettingsEditorComponent, SettingsComponent],
  providers: [ConfigurationService]
})
export class SettingsModule {
}
