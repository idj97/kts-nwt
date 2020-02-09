import { TestBed, async } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { AppComponent } from './app.component';
import { Router } from '@angular/router';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Title } from '@angular/platform-browser';
import { AuthenticationService } from './services/authentication.service';
import { Observable } from 'rxjs';
import { User } from './models/user';

describe('AppComponent', () => {

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

  let router: Router;
  let title: Title;
  let authenticationService: AuthenticationService;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule
      ],
      declarations: [
        AppComponent
      ],
      providers: [
        Title,
        {provide: AuthenticationService, useClass: MockedAuthenticationService},
      ]
    }).compileComponents();
  }));

  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.debugElement.componentInstance;
    expect(app).toBeTruthy();
  });

  it(`should have as title 'm-booking | Homepage'`, () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.debugElement.componentInstance;
    expect(app.title).toEqual('m-booking | Homepage');
  });

  it('should toggle navbar', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.debugElement.componentInstance;
    app.toggleNavbar();
    expect(app.navbarOpen).toBeTruthy();
    app.toggleNavbar();
    expect(app.navbarOpen).toBeFalsy();
  });

  it(`should title be set 'Test'`, () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.debugElement.componentInstance;
    title = TestBed.get(Title);
    title.setTitle('Test');
    expect(title.getTitle()).toBe('Test');
  });

  it(`loggedIn should be true`,() => {
    
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.debugElement.componentInstance;
    
    expect(app.loggedIn).toBeTruthy();
  });

});
