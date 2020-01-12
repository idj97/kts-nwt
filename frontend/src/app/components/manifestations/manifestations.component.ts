import { Component, OnInit } from '@angular/core';
import { UtilityService } from 'src/app/services/utility.service';
import { Title } from '@angular/platform-browser';

@Component({
  selector: 'app-manifestations',
  templateUrl: './manifestations.component.html',
  styleUrls: ['./manifestations.component.css']
})
export class ManifestationsComponent implements OnInit {

  constructor(private utilityService: UtilityService,
    private titleService: Title) { 
      this.titleService.setTitle("m-booking | Manifestations");
    }

  ngOnInit() {
    this.utilityService.resetNavbar();
    document.getElementById("navbar").style.boxShadow = "none";
    document.getElementById("navbar").style.borderBottom = "2px solid black";
    
  }


  focusInput(event: FocusEvent) {
    var el = event.target;
    var parent = (<HTMLElement> el).parentElement;
    var text = <HTMLElement> parent.getElementsByClassName("input-text-value")[0];

    text.style.top = "-6px";
    text.style.fontSize = "12px";
    text.style.color = "darkcyan";
  }

  blurInput(event: FocusEvent) {

    var el = <HTMLInputElement>event.target;

    if (el.value != "") return;

    var parent = (<HTMLElement> el).parentElement;
    var text = <HTMLElement> parent.getElementsByClassName("input-text-value")[0];

    text.style.top = "50%";
    text.style.fontSize = "16px";
    text.style.color = "black";
  }

  

}
