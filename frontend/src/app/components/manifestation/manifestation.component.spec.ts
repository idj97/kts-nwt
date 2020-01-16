import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ManifestationComponent } from './manifestation.component';

describe('ManifestationComponent', () => {
  let component: ManifestationComponent;
  let fixture: ComponentFixture<ManifestationComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ManifestationComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ManifestationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
