import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ManifestationsComponent } from './manifestations.component';
import { AppModule } from 'src/app/app.module';
import { ActivatedRoute } from '@angular/router';
import { UtilityService } from 'src/app/services/utility.service';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { RouterTestingModule } from '@angular/router/testing';

describe('ManifestationsComponent', () => {
  let component: ManifestationsComponent;
  let fixture: ComponentFixture<ManifestationsComponent>;

  class MockedUtilityService {
    resetNavbar () {
      
    }
  }

  class MockedAuthenticationService {
    isLoggedIn() {
      return true;
    }
    isBasicAdmin() {
      return false;
    }
    isSystemAdmin() {
      return false;
    }
    isCustomer() {
      return false;
    }
  }

  beforeEach(async(() => {

    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        AppModule
      ],
      declarations: [ 
      ],
      providers: [
        {provide: UtilityService, useClass: MockedUtilityService},
        {provide: AuthenticationService, useClass: MockedAuthenticationService}
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ManifestationsComponent);
    component = fixture.componentInstance;
    spyOn(document, "getElementById").and.callFake(
      n => document.createElement('div')
    );
    fixture.detectChanges();
  }); 
    


  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('admin should be false', () => {
    const fixture = TestBed.createComponent(ManifestationsComponent);
    const app = fixture.debugElement.componentInstance;

    expect(app.isAdmin).toBeFalsy();
  });

  it(`getSelectedDate() should return '2020-10-20'`, () => {
    const fixture = TestBed.createComponent(ManifestationsComponent);
    const app = fixture.debugElement.componentInstance;

    app.selectedDate = new Date('2020-10-20');
    expect(app.getSelectedDate()).toBe('2020-10-20');
  });

});
