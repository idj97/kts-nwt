import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ManageManifestationComponent } from './manage-manifestation.component';

describe('ManageManifestationComponent', () => {
  let component: ManageManifestationComponent;
  let fixture: ComponentFixture<ManageManifestationComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ManageManifestationComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ManageManifestationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
