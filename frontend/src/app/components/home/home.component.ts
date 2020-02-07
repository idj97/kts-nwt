import { Component, OnInit, HostListener, ViewChild, ElementRef, AfterViewInit } from '@angular/core';
import { AppComponent } from 'src/app/app.component';
import { Router, NavigationEnd } from '@angular/router';
import { UtilityService } from 'src/app/services/utility.service';
import { Title } from '@angular/platform-browser';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  @ViewChild('homeBg1', {static : false}) homeBg1 : ElementRef;

  @ViewChild('scrollAmount', {static : false}) scrollAmount : ElementRef;

  private pageOpacity = 0;

  constructor(
    private router: Router,
    private utilityService: UtilityService,
    private titleService: Title
    ) { 

      this.titleService.setTitle("m-booking | Homepage");
  }

  ngOnInit() {
    this.utilityService.setNavbar();
  }

  ngAfterViewInit() {
    this.render();
  }

  @HostListener('window:scroll', ['$event'])
  onScroll(event) {

    this.utilityService.setNavbar();
  }

  render() {

    this.pageOpacity = 1;
    this.homeBg1.nativeElement.style.transform = "translateY(" + window.pageYOffset * 0.5  + "px)";

    this.scrollAmount.nativeElement.style.width = ((window.pageYOffset / window.innerHeight) * 100 > 100 ? 100 : (window.pageYOffset / window.innerHeight) * 100) + "%";

    requestAnimationFrame(this.render.bind(this));
  }

  enter(event) {
    var el = event.target;
    var bg = el.getElementsByClassName("category-background-holder")[0];
    bg.classList.add("category-mouse-enter");
  }

  leave(event) {
    var el = event.target;
    var bg = el.getElementsByClassName("category-background-holder")[0];
    bg.classList.remove("category-mouse-enter");
  }

}
