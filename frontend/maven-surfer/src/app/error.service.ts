import { Injectable } from '@angular/core';
import { Subject } from "rxjs/Subject";
import { Observable } from "rxjs/Observable";

@Injectable()
export class ErrorService {

  private errorSubject: Subject<string> = new Subject<string>();

  error$: Observable<string> = this.errorSubject.asObservable();

  constructor() { }

  public raiseError(value: string): void {
    this.errorSubject.next(value);
  }

}
