import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../../app/models/user';
import { map } from 'rxjs/operators';
import { PasswordEdit } from '../../app/models/password-edit.model';
import { EditProfileInfo } from '../../app/models/profile-edit-info.model';

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

    getAllUsers(): Observable<Array<User>> {
      return this.http.get<Array<User>>('api/users');
    }

    searchUsers(firstname: string,lastname: string,email: string): Observable<Array<User>> {
      var params = new HttpParams();
      params = params.append("firstname",firstname);
      params = params.append("lastname",lastname);
      params = params.append("email",email);
      return this.http.get<Array<User>>('api/users', {params: params});
    }

    banUser(user: User) {
      return this.http.put(`api/users/ban/${user.id}`, user);
    }

    unBanUser(user: User) {
      return this.http.put(`api/users/unBan/${user.id}`, user);
    }


    changePassword(passwords: PasswordEdit) {
      return this.http.post('api/auth/change_password',passwords);
    }

    changeMyInfo(editInfoData: EditProfileInfo) {
      return this.http.put('api/users', editInfoData);

    }

}
