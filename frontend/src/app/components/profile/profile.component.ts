import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, FormControl } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';


import { ToasterService } from '../../services/toaster.service';
import { AuthenticationService } from '../../services/authentication.service';
import { UserService } from '../../services/user.service';


import { User } from '../../models/user';
import { PasswordEdit } from '../../models/password-edit.model';
import { EditProfileInfo } from '../../models/profile-edit-info.model';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  user: User;
  submitted = false;

  profileForm: FormGroup;
  passwordForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private authenticationService: AuthenticationService,
    private router: Router,
    private toastr: ToasterService,
    private userService: UserService,
    private http: HttpClient
  ) { }

  ngOnInit() {
    this.getUserData();
    this.createForms();
  }

  getUserData() {
    this.user = JSON.parse(localStorage.getItem('user'));
  }

  createForms() {
    this.passwordForm = this.fb.group({
      oldPassword: ['', [Validators.required]],
      newPassword: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(30)]]
    });
    this.profileForm = this.fb.group({
      firstname: [this.user.firstname, [Validators.required]],
      lastname: [this.user.lastname, [Validators.required]]
    });
  }

  onSubmit(): void {
    const data: EditProfileInfo = {
      firstname: this.profileForm.controls['firstname'].value,
      lastname: this.profileForm.controls['lastname'].value
    };

    this.userService.changeMyInfo(data).subscribe(
      _ => this.toastr.showMessage('Success', 'Your profile has been changed.'),
      error => this.toastr.showMessage(error.error.message, 'Error')
    );
  }

  clickEditPassword() {
    const oldPassword = this.passwordForm.controls['oldPassword'].value;
    const newPassword = this.passwordForm.controls['newPassword'].value;

    const passwords: PasswordEdit = {
      oldPassword: oldPassword,
      newPassword: newPassword
    };

    this.submitted = true;

    if (this.passwordForm.invalid) {
      return;
    }

    this.userService.changePassword(passwords).subscribe(
      data => {
        this.authenticationService.logout();
        this.toastr.showMessage('', 'Your password has been changed. Login again.');
        this.router.navigate(['/login']);
      },
      error => {
        this.toastr.showMessage('', error.error.message);
        this.submitted = false;
      }
    );
  }
}
