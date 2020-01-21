import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Observable} from 'rxjs';
import { User } from 'src/app/models/user';
import { map } from "rxjs/operators";
@Injectable({ providedIn: 'root' })
export class UserService {
    constructor(private http: HttpClient) { }

    register(user: User) {
        return this.http.post('api/users/register', user);
    }
   
    edit = (data: User): Observable<boolean> => {
    
        return this.http.put<boolean>("/api/users/editUser", data).pipe(
          map( (res: any) => {
              return res;
          })  );
    }
  
  
   
}

 
 

  