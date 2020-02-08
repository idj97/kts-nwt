import { Injectable } from '@angular/core';
import { Router, ActivatedRouteSnapshot, CanActivate } from '@angular/router';
import { AuthenticationService } from '../services/authentication.service';
import { Location } from '@angular/common';
import { ToasterService } from '../services/toaster.service';

@Injectable({
  providedIn: 'root'
})
export class RoleGuard implements CanActivate {

  constructor(
    private authSvc: AuthenticationService,
    private toaster: ToasterService
    ) {}

  canActivate(route: ActivatedRouteSnapshot): boolean {
    
    const loggedUser = this.authSvc.getCurrentUser();
    let permissions = route.data.permissions as Array<string>;

    if (loggedUser == null || !permissions.includes(loggedUser.authorities[0])) { // the user has only 1 role
      this.toaster.showMessage('Warning', 'You are not allowed to access that page');
      return false;
    }

    return true;
  }
}
