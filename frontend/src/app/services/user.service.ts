import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from 'src/app/models/user';

@Injectable({ providedIn: 'root' })
export class UserService {
    constructor(private http: HttpClient) { }

    register(user: User) {
        return this.http.post('api/users/register', user);
    }

    getAllAdmins(): Observable<Array<User>> {
      return this.http.get<Array<User>>('api/users/admins');
    }

    searchAdmins(firstname: string,lastname: string,email: string): Observable<Array<User>> {
      var params = new HttpParams();
      params = params.append("firstname",firstname);
      params = params.append("lastname",lastname);
      params = params.append("email",email);
      return this.http.get<Array<User>>('api/users/admins', {params: params});
    }





}
