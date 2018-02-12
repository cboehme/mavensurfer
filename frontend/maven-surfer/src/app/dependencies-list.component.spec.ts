import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DependenciesListComponent } from './dependencies-list.component';

describe('DependenciesListComponent', () => {
  let component: DependenciesListComponent;
  let fixture: ComponentFixture<DependenciesListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DependenciesListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DependenciesListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
