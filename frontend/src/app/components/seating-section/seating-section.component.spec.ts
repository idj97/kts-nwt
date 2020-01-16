import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SeatingSectionComponent } from './seating-section.component';

describe('SeatingSectionComponent', () => {
  let component: SeatingSectionComponent;
  let fixture: ComponentFixture<SeatingSectionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SeatingSectionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SeatingSectionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
