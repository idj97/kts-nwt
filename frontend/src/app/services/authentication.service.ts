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

  login(auth: any): Observable<any> {
    return this.http.post('api/auth/login',
      { email: auth.username, password: auth.password },
      { headers: this.headers, responseType: 'json' }
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
    return JSON.parse(localStorage.getItem('user'));
  }

  getBearerToken() {
    return this.getCurrentUser().token;
  }
}
