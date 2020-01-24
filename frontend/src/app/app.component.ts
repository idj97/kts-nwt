import { Component } from '@angular/core';
import { Title } from '@angular/platform-browser';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  title = 'm-booking | Homepage';
  navbarOpen = false;

  public constructor(private titleService: Title ) {
    // this.setTitle(this.title);
  }

  public setTitle( newTitle: string) {
    this.titleService.setTitle( newTitle );
  }

  public toggleNavbar() {
    this.navbarOpen = !this.navbarOpen;
  }

  isLoggedIn() {
    return localStorage.getItem('user') !== null;
  }

}

