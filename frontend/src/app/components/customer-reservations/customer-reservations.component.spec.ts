import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CustomerReservationsComponent } from './customer-reservations.component';

describe('CustomerReservationsComponent', () => {
  let component: CustomerReservationsComponent;
  let fixture: ComponentFixture<CustomerReservationsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CustomerReservationsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CustomerReservationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
