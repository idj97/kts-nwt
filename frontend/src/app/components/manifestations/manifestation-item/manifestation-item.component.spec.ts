import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ManifestationItemComponent } from './manifestation-item.component';

describe('ManifestationItemComponent', () => {
  let component: ManifestationItemComponent;
  let fixture: ComponentFixture<ManifestationItemComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ManifestationItemComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ManifestationItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
