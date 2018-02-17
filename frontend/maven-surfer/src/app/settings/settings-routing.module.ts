import { NgModule } from '@angular/core';
import { RouterModule, Routes } from "@angular/router";

import { SettingsComponent } from "./settings.component";
import { SettingsEditorComponent } from "./settings-editor.component";

const settingsRoutes: Routes = [
  {
    path: 'settings',
    component: SettingsComponent,
    children: [
      { path: '', component: SettingsEditorComponent }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(settingsRoutes)],
  exports: [RouterModule]
})
export class SettingsRoutingModule {
}
