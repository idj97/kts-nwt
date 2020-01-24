import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ManageManifestationSectionsComponent } from './manage-manifestation-sections.component';

describe('ManageManifestationSectionsComponent', () => {
  let component: ManageManifestationSectionsComponent;
  let fixture: ComponentFixture<ManageManifestationSectionsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ManageManifestationSectionsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ManageManifestationSectionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
