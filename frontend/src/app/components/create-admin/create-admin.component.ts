import { Component, OnInit } from '@angular/core';
import { FormGroup, Form, FormBuilder } from '@angular/forms';
import { Router } from '@angular/router';

import { User } from '../../models/user';
import { UserService } from '../../services/user.service';
import { ToasterService } from '../../services/toaster.service';

@Component({ 
  selector: 'app-create-admin',
  templateUrl: './create-admin.component.html',
  styleUrls: ['./create-admin.component.css']
})
export class CreateAdminComponent implements OnInit {
  createAdminForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private userService: UserService,
    private toasterService: ToasterService
  ) {
    this.createAdminForm = this.fb.group({
      email: '',
      firstname: '',
      lastname: '',
      password: ''
    });
  }

  ngOnInit() {
  }

  onSubmit(admin: User) {
    this.userService.createAdmin(admin).subscribe(
      _ => {
        this.toasterService.showMessage('Success', 'Admin created successfully!');
        this.router.navigate(['/manage-admins']);
      },
      error => this.toasterService.showMessage('', error.error.message)
    );
  }

}
