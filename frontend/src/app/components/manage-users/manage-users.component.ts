import { Component, OnInit } from '@angular/core';
import { UtilityService } from '../../services/utility.service';
import { User } from '../../models/user';
import { UserService } from '../../services/user.service';
import { ToasterService } from '../../services/toaster.service';

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

  constructor(
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
    this.getAllUsers();
  }

  private searchUsers() {
    this.searchEmail = ((document.getElementById('searchEmail')) as HTMLInputElement).value.trim();
    this.searchFirstname = ((document.getElementById('searchFirstname')) as HTMLInputElement).value.trim();
    this.searchLastname = ((document.getElementById('searchLastname')) as HTMLInputElement).value.trim();
    this.userService
      .searchUsers(this.searchFirstname, this.searchLastname, this.searchEmail)
      .subscribe(data => {
        const searchedUsers = data;
        this.users = searchedUsers.page;
      });
  }

  private getAllUsers() {
    this.userService.getAllUsers().subscribe(data => {
      const usersList = data;
      this.users = usersList.page;
    });
  }

  private banUser(user: User) {
    this.userService.banUser(user).subscribe(data => {
      this.toastrService.showMessage('Success', 'User is banned.');
      this.getAllUsers();
    });
  }

  private unBanUser(user: User) {
    this.userService.unBanUser(user).subscribe(data => {
      this.toastrService.showMessage('Success', 'User is unbanned.');
      this.getAllUsers();
    });
  }

  focusInput(event: FocusEvent) {
    const el = event.target;
    const parent = (el as HTMLElement).parentElement;
    const text = (
      parent.getElementsByClassName('input-text-value')[0]
    ) as HTMLElement;

    text.style.top = '-6px';
    text.style.fontSize = '12px';
    text.style.color = 'darkcyan';
  }

  blurInput(event: FocusEvent) {
    const el = event.target as HTMLInputElement;

    if (el.value !== '') { return; }

    const parent = (el as HTMLElement).parentElement;
    const text = (
      parent.getElementsByClassName('input-text-value')[0]
    ) as HTMLElement;

    text.style.top = '50%';
    text.style.fontSize = '16px';
    text.style.color = 'black';
  }
}
