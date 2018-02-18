import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { ImportComponent } from "./import.component";
import { ProjectImporterComponent } from "./project-importer.component";

const routes: Routes = [
  {
    path: 'import',
    component: ImportComponent,
    children: [
      { path: '', redirectTo: '/import/project', pathMatch: 'full' },
      { path: 'project', component: ProjectImporterComponent }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ImportRoutingModule {
}
