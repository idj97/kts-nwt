import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ManageManifestationDaysComponent } from './manage-manifestation-days.component';

describe('ManageManifestationDaysComponent', () => {
  let component: ManageManifestationDaysComponent;
  let fixture: ComponentFixture<ManageManifestationDaysComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ManageManifestationDaysComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ManageManifestationDaysComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
