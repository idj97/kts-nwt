import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class UtilityService {

  constructor() { }


  public setNavbar() {
    

    if (window.pageYOffset > 0) {
      this.resetNavbar();
    }
    else {
      var navbar = document.getElementById("navbar");
      var navLinks = document.getElementsByClassName("nav-link");
      var navBrand = document.getElementsByClassName("navbar-brand")[0];
      var navBarToggler = document.getElementsByClassName("navbar-toggler-icon")[0];
      navbar.style.borderBottom = "none";
      navbar.classList.remove("bg-white");
      navbar.style.boxShadow = "0px 0px 15px 0px rgba(0,0,0,0.0)";
      navBrand.classList.remove("nav-black-color");
      navBrand.classList.add("nav-white-color");
      navBarToggler.classList.remove("nav-bar-toggler-icon-black");
      for (var i = 0; i < navLinks.length; i++) {
        navLinks[i].classList.remove("nav-black-color");
        navLinks[i].classList.add("nav-white-color");
      }
    }
  }

  public resetNavbar() {
    
    var navbar = document.getElementById("navbar");
    var navLinks = document.getElementsByClassName("nav-link");
    var navBrand = document.getElementsByClassName("navbar-brand")[0];
    var navBarToggler = document.getElementsByClassName("navbar-toggler-icon")[0];
    navbar.style.borderBottom = "none";
    navbar.classList.add("bg-white");
    navbar.style.boxShadow = "0px 0px 15px 0px rgba(0,0,0,0.5)";
    navBrand.classList.remove("nav-white-color");
    navBrand.classList.add("nav-black-color");
    navBarToggler.classList.add("nav-bar-toggler-icon-black");
    for (var i = 0; i < navLinks.length; i++) {
      navLinks[i].classList.remove("nav-white-color");
      navLinks[i].classList.add("nav-black-color");
    }
  }

}
