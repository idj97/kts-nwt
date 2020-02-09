import { TestBed } from '@angular/core/testing';

import { AuthenticationService } from './authentication.service';
import { HttpTestingController, HttpClientTestingModule } from '@angular/common/http/testing';

fdescribe('AuthenticationService', () => {

  let service: AuthenticationService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [AuthenticationService],
      imports: [HttpClientTestingModule]
    });
    service = TestBed.get(AuthenticationService);
    httpMock = TestBed.get(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('login should return error for username', () => {
    const auth: any = {
      username : "WrongUsername",
      password : "user"
    };
    service.login(auth).subscribe(data => {
      expect(JSON.parse(data).status).toEqual(406);
      expect(JSON.parse(data).statusText).toEqual(`User with username ${auth.username} not found`);
      
    });
    const loginUrl =  "api/auth/login";
    const req = httpMock.expectOne(loginUrl);
    expect(req.request.method).toBe('POST');
    req.flush({
      status: 406,
      statusText: `User with username ${auth.username} not found`
    });
    httpMock.verify();
  });

  it('login should return error for password', () => {
    const auth: any = {
      username : "ktsnwt.customer@gmail.com",
      password : "123aaaaaaaaaaaaaaaaaaa"
    };
    service.login(auth).subscribe(data => {
      expect(JSON.parse(data).status).toEqual(406);
      expect(JSON.parse(data).statusText).toEqual(`Wrong password`);
      
    });
    const loginUrl =  "api/auth/login";
    const req = httpMock.expectOne(loginUrl);
    expect(req.request.method).toBe('POST');
    req.flush({
      status: 406,
      statusText: `Wrong password`
    });
    httpMock.verify();
  });


  it('login should return jwt token', () => {
    const auth: any = {
      username : "ktsnwt.customer@gmail.com",
      password : "user"
    };
    service.login(auth).subscribe(data => {
      expect(data).toEqual("jwttoken");
    });
    const loginUrl =  "api/auth/login";
    const req = httpMock.expectOne(loginUrl);
    expect(req.request.method).toBe('POST');
    req.flush("jwttoken");
    httpMock.verify();
  });

  it('login should return the user info', () => {
    const auth: any = {
      username : "ktsnwt.customer@gmail.com",
      password : "user"
    };

    service.login(auth).subscribe(data => {
      expect(data.username).toEqual(auth.username);
    });

    const req = httpMock.expectOne('api/auth/login');
    expect(req.request.method).toBe('POST');
    req.flush(auth);
  });

  it('login should return error', () => {
    const auth: any = {
      username : "ktsnwtaaa.customer@gmail.com",
      password : "usaaer"
    };

    service.login(auth).subscribe(data => {
      expect(data.status).toEqual(400);
      expect(data.statusText).toEqual('Bad credentials');
    });

    const req = httpMock.expectOne('api/auth/login');
    expect(req.request.method).toBe('POST');
    req.flush({
      status: 400, 
      statusText: 'Bad credentials'
    });
  });









})
