import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { ClarityModule } from "@clr/angular";

import { SettingsRoutingModule } from './settings-routing.module';

import { SettingsComponent } from './settings.component';
import { SettingsEditorComponent } from './settings-editor.component';

import { ConfigurationService } from './configuration.service';

@NgModule({
  imports: [
    CommonModule,
    ClarityModule,
    FormsModule,
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
