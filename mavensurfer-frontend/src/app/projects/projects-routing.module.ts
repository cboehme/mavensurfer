import { NgModule } from '@angular/core';
import { RouterModule, Routes } from "@angular/router";

import { ProjectsComponent } from "./projects.component";
import { DashboardComponent } from "./dashboard.component";
import { ProjectComponent } from "./project.component";

const projectsRoutes: Routes = [
  {
    path: 'projects',
    component: ProjectsComponent,
    children: [
      { path: '', component: DashboardComponent },
      { path: ':groupId/:artifactId/:version', component: ProjectComponent }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(projectsRoutes)],
  exports: [RouterModule]
})
export class ProjectsRoutingModule {
}
