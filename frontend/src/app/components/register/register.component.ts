import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';

import { ToasterService } from '../../services/toaster.service';
import { UserService } from '../../services/user.service';
import { User } from '../../models/user';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  registerForm: FormGroup;
  loading = false;
  submitted = false;
  user: User = new User();

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private toastr: ToasterService,
    private userService: UserService
  ) {}
  ngOnInit() {
    this.registerForm = this.fb.group({
      password: [
        this.user.password,
        [Validators.required, Validators.minLength(5), Validators.maxLength(30)]
      ],

      firstname: [this.user.firstname, [Validators.required]],
      lastname: [this.user.lastname, [Validators.required]],

      email: [this.user.email, [Validators.required, Validators.email]]
    });
  }

  get f() {
    return this.registerForm.controls;
  }

  onSubmit(): void {
    this.submitted = true;
    if (this.registerForm.invalid) {
      return;
    }

    this.loading = true;
    this.userService
      .register(this.registerForm.value)
      .subscribe(
        data => {
          this.toastr.showErrorMessage('Successful login!');
          this.router.navigate(['/login']);
        },
        error => {
          this.toastr.showMessage(error.error.message, 'Warning');
          this.loading = false;
        }
      );
  }
}
