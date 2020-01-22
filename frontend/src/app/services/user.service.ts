import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Observable} from 'rxjs';
import { User } from 'src/app/models/user';
import { map } from "rxjs/operators";
import{PasswordEdit} from 'src/app/models/password-edit.model'

import {EditProfileInfo} from 'src/app/models/profile-edit-info.model'

@Injectable({ providedIn: 'root' })
export class UserService {
  
    constructor(private http: HttpClient) { }

    register(user: User) {
        return this.http.post('api/users/register', user);
    }
   
    getProfileData(): Observable<any> {
      return this.http.get('api/users/getLogged');    
    }
  
   /**  getUser():Observable<User>{
        return this.http.get<User>('api/isLoggedIn');
      }*/
    
    
    changePassword(passwords:PasswordEdit){
      return this.http.post('api/auth/change_password',passwords);
      }

    changeMyInfo(editInfoData:EditProfileInfo) {
      return this.http.put('api/users/editUser', editInfoData);

    }

    logout(): void {
        localStorage.removeItem('user');
      }
   
}