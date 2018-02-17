import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { ImportComponent } from "./import.component";

const routes: Routes = [
  {
    path: 'import',
    component: ImportComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ImportRoutingModule {
}
