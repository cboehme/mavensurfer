import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ClarityModule } from "@clr/angular";

import { ImportRoutingModule } from "./import-routing.module";

import { ImportComponent } from './import.component';

@NgModule({
  imports: [
    CommonModule,
    ClarityModule,
    ImportRoutingModule
  ],
  declarations: [
    ImportComponent
  ]
})
export class ImportModule { }
