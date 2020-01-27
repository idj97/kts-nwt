import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UtilityService } from 'src/app/services/utility.service';
import { User } from 'src/app/models/user';
import { UserService } from 'src/app/services/user.service';
import { AuthenticationService } from 'src/app/services/authentication.service';


@Component({
  selector: 'app-manage-users',
  templateUrl: './manage-users.component.html',
  styleUrls: ['./manage-users.component.css']
})
export class ManageUsersComponent implements OnInit {

  private users: User[];
  private searchEmail: string;
  private searchFirstname: string;
  private searchLastname: string;

  constructor(private route: ActivatedRoute,private utilityService: UtilityService, private authService: AuthenticationService, private userService: UserService) {
  this.utilityService.setNavbar();
 }

  ngOnInit() {
    this.utilityService.resetNavbar();
    document.getElementById("navbar").style.boxShadow = "none";
    document.getElementById("navbar").style.borderBottom = "2px solid black";
    this.getAllUsers();
  }

  private searchUsers() {
    this.searchEmail = (<HTMLInputElement>document.getElementById("searchEmail")).value;
    this.searchFirstname = (<HTMLInputElement>document.getElementById("searchFirstname")).value;
    this.searchLastname = (<HTMLInputElement>document.getElementById("searchLastname")).value;
    this.userService.searchAdmins(this.searchFirstname,this.searchLastname,this.searchEmail).subscribe(data => {var searchedAdmins = data; console.log(searchedAdmins['page']); this.admins = searchedAdmins['page']})
  }

  private getAllUsers() {

    this.userService.getAllUsers().subscribe(data => {var usersList = data; this.users = usersList['page']})
  }

  private blockUser() {
    alert("Block not implemented!!!");
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
