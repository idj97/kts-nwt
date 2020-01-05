import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthenticationService } from '../services/authentication.service';
import { Injectable } from '@angular/core';

@Injectable()
export class TokenInterceptor implements HttpInterceptor {

    constructor(private authService: AuthenticationService) {

    }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        
        if(this.authService.isLoggedIn) {
            
            req = req.clone({
                setHeaders: {
                    Authorization: `Bearer ${this.authService.getDummyToken()}`
                }
            });
        }

        return next.handle(req);
        
    }

}