import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DependantsListComponent } from './dependants-list.component';

describe('DependantsListComponent', () => {
  let component: DependantsListComponent;
  let fixture: ComponentFixture<DependantsListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DependantsListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DependantsListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
