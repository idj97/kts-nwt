import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ToastrService } from 'ngx-toastr';
import { LoginComponent } from './login.component';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { of } from 'rxjs';
import { Router } from '@angular/router';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authenticationService : AuthenticationService;
  let router: Router;
  let toastr: ToastrService;

  beforeEach(async(() => {
    let authenticationServiceMocked = {
        login: jasmine.createSpy('login').and.returnValue(of({
        username: 'ktsnwt.customer@gmail.com',
        password: 'user'
      }))
    }
    let routerMocked = jasmine.createSpyObj('router', ['navigate']);

    let toastrMocked = jasmine.createSpyObj('toastr', ['success']);

    TestBed.configureTestingModule({
      imports: [FormsModule,ReactiveFormsModule],
      declarations: [ LoginComponent ],
      providers: [
        { provide: AuthenticationService, useValue: authenticationServiceMocked },
        { provide: Router, useValue: routerMocked },
        { provide: ToastrService, useValue: toastrMocked }
      ]
      
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    authenticationService = TestBed.get(AuthenticationService);
    router = TestBed.get(Router);
    toastr = TestBed.get(ToastrService);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should be initialized', () => {
    component.ngOnInit();
    expect(component.loginForm).toBeDefined();
    expect(component.loginForm.invalid).toBeTruthy();
  });

  it('should be invalid form when inputs are empty', () => {
    component.loginForm.controls.username.setValue('');
    component.loginForm.controls.password.setValue('');
    expect(component.loginForm.invalid).toBeTruthy();
  });

  it('should be invalid form when username is empty', () => {
    component.loginForm.controls.username.setValue('');
    component.loginForm.controls.password.setValue('user');
    expect(component.loginForm.invalid).toBeTruthy();
  });

  it('should be invalid form when password is empty', () => {
    component.loginForm.controls.username.setValue('ktsnwt.customer@gmail.com');
    component.loginForm.controls.password.setValue('');
    expect(component.loginForm.invalid).toBeTruthy();
  });

  it('should login on submit', () => {
    component.loginForm.controls.username.setValue('ktsnwt.customer@gmail.com');
    component.loginForm.controls.password.setValue('user');
    component.onSubmit();

    expect(component.loginForm.invalid).toBeFalsy();
    /**expect(localStorage.getItem('user')).toEqual('ktsnwt.customer@gmail.com');*/
   /** expect(router.navigate).toHaveBeenCalledWith(['/home']);*/
    
  });
});
