import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ManageManifestationImagesComponent } from './manage-manifestation-images.component';

describe('ManageManifestationImagesComponent', () => {
  let component: ManageManifestationImagesComponent;
  let fixture: ComponentFixture<ManageManifestationImagesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ManageManifestationImagesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ManageManifestationImagesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
