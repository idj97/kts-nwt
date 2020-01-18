import { Injectable } from '@angular/core';
import { HttpHeaders, HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  private headers = new HttpHeaders({'Content-Type': 'application/json'});
  
  constructor(
		private http: HttpClient
	) { }

  getBearerToken() {
    return window.localStorage.getItem('user-token');
  }

  
  login(auth: any): Observable<any> {
		return this.http.post('api/auth/login', {username: auth.username, password: auth.password}, {headers: this.headers, responseType: 'text'});
  }
  
  


  
  isLoggedIn(): boolean {
      if (!localStorage.getItem('user')) {
          return false;
      }
      return true;
    }



    logout(): Observable<any> {
      return this.http.get('api/logOut', {headers: this.headers, responseType: 'text'});
    }


  // temporary dummy method to use before we implement login
  getDummyToken() {
    return 'eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJjb25uZWN0ZWRhcHAiLCJzdWIiOiJzeXNhZG1pbkBleGFtcGxlLmNvbSJ9.ZI20cfHkiu_rsdhzHpp1yStHOsEfuthLlrk1IVpXa_4';
  }
}
