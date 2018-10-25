import { Component, OnInit } from '@angular/core';
import { Settings } from "./settings";
import { ConfigurationService } from "./configuration.service";
import { log } from "util";

@Component({
  selector: 'app-settings-editor',
  templateUrl: './settings-editor.component.html',
  styles: []
})
export class SettingsEditorComponent implements OnInit {

  model: Settings = new Settings();

  saveFailed: boolean = false;
  saveOk: boolean = false;

  constructor(private configurationService: ConfigurationService) {
  }

  ngOnInit() {
    log("Settings");
    this.configurationService.getSettings()
      .subscribe(settings => this.model = settings,
        error => { this.model = new Settings(); });
  }

  saveSettings() {
    this.saveFailed = false;
    this.saveOk = false;
    this.configurationService.putSettings(this.model)
      .subscribe(
        () => {
          this.saveFailed = false;
          this.saveOk = true;
        },
        () => {
          this.saveFailed = true;
          this.saveOk = false;
        }
      );
  }

}
