import { Injectable } from '@angular/core';
import { HttpHeaders, HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../models/user';
@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  private headers = new HttpHeaders({ 'Content-Type': 'application/json' });

  constructor(private http: HttpClient) {}

  getBearerToken() {
    return window.localStorage.getItem('user-token');
  }

  login(auth: any): Observable<any> {
    return this.http.post('api/auth/login',
      { email: auth.username, password: auth.password },
      { headers: this.headers, responseType: 'text' }
    );
  }

  logout(): void {
    localStorage.removeItem('user');
  }

  isLoggedIn(): boolean {
    return localStorage.getItem('user') !== null;
  }

  isSystemAdmin(): boolean {
    if (this.isLoggedIn()) {
      return this.getCurrentUser().authorities.includes('ROLE_SYS_ADMIN');
    }
    return false;
  }

  isBasicAdmin(): boolean {
    if (this.isLoggedIn()) {
      return this.getCurrentUser().authorities.includes('ROLE_ADMIN');
    }
    return false;
  }

  getCurrentUser(): User {
    return JSON.parse(JSON.parse(localStorage.getItem('user')));
  }

  // temporary dummy method to use before we implement login
  getDummyToken() {
    //return 'eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJjb25uZWN0ZWRhcHAiLCJzdWIiOiJzeXNhZG1pbkBleGFtcGxlLmNvbSJ9.ZI20cfHkiu_rsdhzHpp1yStHOsEfuthLlrk1IVpXa_4';
    return 'eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJjb25uZWN0ZWRhcHAiLCJzdWIiOiJrdHNud3QuY3VzdG9tZXJAZ21haWwuY29tIn0.x3r3rrRX1f7JomHamPRrfosd8DXu33o_VdGzTGwwJJM';
  }
}
