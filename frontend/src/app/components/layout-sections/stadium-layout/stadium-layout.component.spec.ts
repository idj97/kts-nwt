import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { StadiumLayoutComponent } from './stadium-layout.component';

describe('StadiumLayoutComponent', () => {
  let component: StadiumLayoutComponent;
  let fixture: ComponentFixture<StadiumLayoutComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ StadiumLayoutComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StadiumLayoutComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
