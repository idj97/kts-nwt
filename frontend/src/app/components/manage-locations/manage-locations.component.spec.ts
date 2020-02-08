import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ManageLocationsComponent } from './manage-locations.component';

describe('ManageLocationsComponent', () => {
  let component: ManageLocationsComponent;
  let fixture: ComponentFixture<ManageLocationsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ManageLocationsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ManageLocationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
