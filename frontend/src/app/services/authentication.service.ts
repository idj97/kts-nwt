import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  constructor() { }

  getBearerToken() {
    return window.localStorage.getItem('user-token');
  }

  isLoggedIn() {
    return true;
  }

  // temporary dummy method to use before we implement login
  getDummyToken() {
    //return 'eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJjb25uZWN0ZWRhcHAiLCJzdWIiOiJzeXNhZG1pbkBleGFtcGxlLmNvbSJ9.ZI20cfHkiu_rsdhzHpp1yStHOsEfuthLlrk1IVpXa_4';
    return 'eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJjb25uZWN0ZWRhcHAiLCJzdWIiOiJrdHNud3QuY3VzdG9tZXJAZ21haWwuY29tIn0.x3r3rrRX1f7JomHamPRrfosd8DXu33o_VdGzTGwwJJM';
  }
}
