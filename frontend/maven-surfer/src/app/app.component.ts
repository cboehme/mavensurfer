import { Component, OnInit } from '@angular/core';

import '@clr/icons';
import '@clr/icons/shapes/essential-shapes'
import '@clr/icons/shapes/technology-shapes'

import { ErrorService } from "./error.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html'
})
export class AppComponent implements OnInit {

  showErrorMessage: boolean = true;
  errorMessage: string = "";

  constructor(private errorService: ErrorService) {}

  ngOnInit() {
    this.errorService.error$.subscribe(
      value => {
        this.showErrorMessage = true;
        this.errorMessage = value;
      }
    );
  }

}
