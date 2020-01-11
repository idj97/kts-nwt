import { TestBed } from '@angular/core/testing';

import { ManifestationService } from './manifestation.service';

describe('ManifestationService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: ManifestationService = TestBed.get(ManifestationService);
    expect(service).toBeTruthy();
  });
});
