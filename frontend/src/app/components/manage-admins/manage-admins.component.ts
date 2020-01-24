import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UtilityService } from 'src/app/services/utility.service';

@Component({
  selector: 'app-manage-admins',
  templateUrl: './manage-admins.component.html',
  styleUrls: ['./manage-admins.component.css']
})
export class ManageAdminsComponent implements OnInit {

  constructor(private route: ActivatedRoute,private utilityService: UtilityService) {
  this.utilityService.setNavbar();
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
