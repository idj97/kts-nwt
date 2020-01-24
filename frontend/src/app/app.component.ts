import { Component } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { Router, NavigationEnd } from '@angular/router';
import { User } from './models/user';
import { AuthenticationService } from './services/authentication.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent{
  title = 'm-booking | Homepage';
  navbarOpen = false;

  loggedIn: boolean;
  basicAdmin: boolean;
  systemAdmin: boolean;

  public constructor(
    private router: Router,
    private authenticationService: AuthenticationService,
    private titleService: Title
  ) {
    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        this.updateUserRoleInformation();
      }
    });
  }

  updateUserRoleInformation() {
    this.loggedIn = this.authenticationService.isLoggedIn();
    this.basicAdmin = this.authenticationService.isBasicAdmin();
    this.systemAdmin = this.authenticationService.isSystemAdmin();
  }

  logout() {
    this.authenticationService.logout();
    window.location.reload();
  }

  setTitle( newTitle: string) {
    this.titleService.setTitle( newTitle );
  }

  toggleNavbar() {
    this.navbarOpen = !this.navbarOpen;
  }
}
