import { Injectable } from '@angular/core';
import { Subject } from "rxjs/Subject";
import { Observable } from "rxjs/Observable";

@Injectable()
export class ErrorService {

  private readonly errorSubject: Subject<string>;

  readonly error$: Observable<string>;

  constructor() {
    this.errorSubject = new Subject();
    this.error$ = this.errorSubject.asObservable();
  }

  public raiseError(value: string): void {
    this.errorSubject.next(value);
  }

}
