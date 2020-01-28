import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { UtilityService } from '../../services/utility.service';
import { User } from '../../models/user';
import { UserService } from '../../services/user.service';
import { ToasterService } from '../../services/toaster.service';

@Component({
  selector: 'app-manage-admins',
  templateUrl: './manage-admins.component.html',
  styleUrls: ['./manage-admins.component.css']
})
export class ManageAdminsComponent implements OnInit {
  private admins: User[];
  private searchEmail: string;
  private searchFirstname: string;
  private searchLastname: string;

  constructor(
    private router: Router,
    private utilityService: UtilityService,
    private userService: UserService,
    private toastrService: ToasterService
  ) {
    this.utilityService.setNavbar();
  }

  ngOnInit() {
    this.utilityService.resetNavbar();
    document.getElementById('navbar').style.boxShadow = 'none';
    document.getElementById('navbar').style.borderBottom = '2px solid black';
    this.getAllAdmins();
  }

  private searchAdmins() {
    this.searchEmail = ((document.getElementById('searchEmail')) as HTMLInputElement).value.trim();
    this.searchFirstname = ((document.getElementById('searchFirstname')) as HTMLInputElement).value.trim();
    this.searchLastname = ((document.getElementById('searchLastname')) as HTMLInputElement).value.trim();
    this.userService
      .searchAdmins(this.searchFirstname, this.searchLastname, this.searchEmail)
      .subscribe(data => {
        const searchedAdmins = data;
        this.admins = searchedAdmins.page;
      });
  }

  private getAllAdmins() {
    this.userService.getAllAdmins().subscribe(data => {
      this.admins = data.page;
    });
  }

  private deleteAdmin() {
    alert('Delete not implemented!!!');
  }

  private addNewAdmin() {
    this.router.navigateByUrl('/create-admin');
  }

  focusInput(event: FocusEvent) {
    const el = event.target;
    const parent = ( el as HTMLElement).parentElement;
    const text =  (parent.getElementsByClassName('input-text-value')[0]) as HTMLElement;

    text.style.top = '-6px';
    text.style.fontSize = '12px';
    text.style.color = 'darkcyan';
  }

  blurInput(event: FocusEvent) {
    const el =  event.target as HTMLInputElement;

    if (el.value !== '') { return; }

    const parent = ( el as HTMLElement).parentElement;
    const text =  ( parent.getElementsByClassName('input-text-value')[0]) as HTMLElement;

    text.style.top = '50%';
    text.style.fontSize = '16px';
    text.style.color = 'black';
  }
}
