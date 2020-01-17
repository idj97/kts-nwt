import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { StandingSectionComponent } from './standing-section.component';

describe('StandingSectionComponent', () => {
  let component: StandingSectionComponent;
  let fixture: ComponentFixture<StandingSectionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ StandingSectionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StandingSectionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
