import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OpenSpaceLayoutComponent } from './open-space-layout.component';

describe('OpenSpaceLayoutComponent', () => {
  let component: OpenSpaceLayoutComponent;
  let fixture: ComponentFixture<OpenSpaceLayoutComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ OpenSpaceLayoutComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OpenSpaceLayoutComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
