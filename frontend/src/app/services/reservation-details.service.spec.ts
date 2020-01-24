import { TestBed } from '@angular/core/testing';

import { ReservationDetailsService } from './reservation-details.service';

describe('ReservationDetailsService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: ReservationDetailsService = TestBed.get(ReservationDetailsService);
    expect(service).toBeTruthy();
  });
});
