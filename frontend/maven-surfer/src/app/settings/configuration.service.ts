import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs/Observable";
import "rxjs/add/operator/map";

import {Settings} from "./settings";
import {log} from "util";

@Injectable()
export class ConfigurationService {

  private baseUrl = 'http://localhost:8080/api/configuration';

  constructor(private http: HttpClient) { }

  getSettings(): Observable<Settings> {
    return this.http.get(this.baseUrl + "/settings", {responseType: 'text'})
      .map(settingsXml => new Settings(settingsXml));
  }

  putSettings(settings: Settings) {
    return this.http.put(this.baseUrl + "/settings", settings.settingsXml,
      { headers: { "Content-Type": "application/xml" }});
  }

}
