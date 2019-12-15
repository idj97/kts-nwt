import { Component, OnInit, HostListener, ViewChild, ElementRef, AfterViewInit } from '@angular/core';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  @ViewChild('homeBg1', {static : false}) homeBg1 : ElementRef;

  @ViewChild('homeBg2Holder', {static : false}) homeBg2Holder : ElementRef;
  @ViewChild('homeBg2', {static : false}) homeBg2 : ElementRef;

  @ViewChild('homeBg3Holder', {static : false}) homeBg3Holder : ElementRef;
  @ViewChild('homeBg3', {static : false}) homeBg3 : ElementRef;

  @ViewChild('homeBg4Holder', {static : false}) homeBg4Holder : ElementRef;
  @ViewChild('homeBg4', {static : false}) homeBg4 : ElementRef;

  constructor() { }

  ngOnInit() {
    this.setNavbar();
  }

  ngAfterViewInit() {
    this.render();
  }

  @HostListener('window:scroll', ['$event'])
  onScroll(event) {

    
    this.setNavbar();
  }

  render() {
    this.homeBg1.nativeElement.style.transform = "translateY(" + window.pageYOffset * 0.5  + "px)";
    //this.homeBg1.nativeElement.style.backgroundPositionY = window.pageYOffset * 0.5  + "px";

    var homebg2Offset = this.homeBg2Holder.nativeElement.getBoundingClientRect().top + window.pageYOffset;
    this.homeBg2.nativeElement.style.transform = "translateY(" + (window.pageYOffset - homebg2Offset) * 0.6  + "px)";

    var homebg3Offset = this.homeBg3Holder.nativeElement.getBoundingClientRect().top + window.pageYOffset;
    this.homeBg3.nativeElement.style.transform = "translateY(" + (window.pageYOffset - homebg3Offset) * 0.6  + "px)";

    var homebg4Offset = this.homeBg4Holder.nativeElement.getBoundingClientRect().top + window.pageYOffset;
    this.homeBg4.nativeElement.style.transform = "translateY(" + (window.pageYOffset - homebg4Offset) * 0.6  + "px)";
    requestAnimationFrame(this.render.bind(this));
  }

  setNavbar() {
    var navbar = document.getElementById("navbar");
    var navLinks = document.getElementsByClassName("nav-link");
    var navBrand = document.getElementsByClassName("navbar-brand")[0];
    var navBarToggler = document.getElementsByClassName("navbar-toggler-icon")[0];

    if (window.pageYOffset > 0) {
      navbar.classList.add("bg-white");
      navbar.style.boxShadow = "0px 0px 15px 0px rgba(0,0,0,0.9)";
      navBrand.classList.remove("nav-white-color");
      navBrand.classList.add("nav-black-color");
      navBarToggler.classList.add("nav-bar-toggler-icon-black");
      for (var i = 0; i < navLinks.length; i++) {
        navLinks[i].classList.remove("nav-white-color");
        navLinks[i].classList.add("nav-black-color");
      }
    }
    else {
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

}
