import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {FormsModule} from '@angular/forms';
import {BrowserModule} from "@angular/platform-browser";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {ClarityModule} from "@clr/angular";
import { SettingsComponent } from './settings.component';
import {ConfigurationService} from './configuration.service';

@NgModule({
  imports: [
    CommonModule,
    BrowserModule,
    FormsModule,
    BrowserAnimationsModule,
    ClarityModule
  ],
  declarations: [SettingsComponent],
  providers: [ConfigurationService]
})
export class SettingsModule { }
