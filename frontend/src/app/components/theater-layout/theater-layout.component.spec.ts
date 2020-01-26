import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TheaterLayoutComponent } from './theater-layout.component';

describe('TheaterLayoutComponent', () => {
  let component: TheaterLayoutComponent;
  let fixture: ComponentFixture<TheaterLayoutComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TheaterLayoutComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TheaterLayoutComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
