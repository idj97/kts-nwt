import { Component, OnInit } from '@angular/core';
import { first } from 'rxjs/operators';
import { FormBuilder,FormGroup, Validators } from '@angular/forms';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { Router } from '@angular/router';
import { ToasterService } from 'src/app/services/toaster.service';
import { HttpClient } from '@angular/common/http';
import { AlertService} from 'src/app/services/alert.service';
import {UserService} from 'src/app/services/user.service';

import {User} from 'src/app/models/user'

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  registerForm:FormGroup;
  loading = false;
  submitted = false;
  user: User = new User();
  


    
  constructor(
    private fb: FormBuilder,
		private authenticationService: AuthenticationService,
		private router: Router,
    private toastr: ToasterService,
    private userService: UserService,
    private http: HttpClient
  ) {}
  ngOnInit() {
    
    this.registerForm = this.fb.group({
      'username': [this.user.username, [
        Validators.required
      ]],
      'password': [this.user.password, [
        Validators.required,
        Validators.minLength(5),
        Validators.maxLength(30)
      ]],
      
      'firstname': [this.user.firstname, [
        Validators.required
      ]],
      'lastname': [this.user.lastname, [
        Validators.required
      ]],
      
      'email': [this.user.email, [
        Validators.required,
        Validators.email
      ]],
     
    });
  }

  get f() { return this.registerForm.controls; }
  onSubmit():void{
    this.submitted = true;
    if (this.registerForm.invalid) {
      return;
  }

  this.loading = true;
  this.userService.register(this.registerForm.value)
      
      .subscribe(
          data => {
            this.toastr.showErrorMessage('Successful login!');
              this.router.navigate(['/login']);
          },
          error => {
            this.toastr.showMessage(error.error.message,'Warning');
            this.loading = false;
          });
}
  
}


