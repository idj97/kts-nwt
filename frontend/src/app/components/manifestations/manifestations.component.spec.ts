import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ManifestationsComponent } from './manifestations.component';

describe('ManifestationsComponent', () => {
  let component: ManifestationsComponent;
  let fixture: ComponentFixture<ManifestationsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ManifestationsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ManifestationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
