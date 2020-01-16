import { Component, OnInit } from '@angular/core';

import { first } from 'rxjs/operators';
import { FormBuilder, Validators } from '@angular/forms';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';

import { AlertService} from 'src/app/services/alert.service';
import { UtilityService } from 'src/app/services/utility.service';


@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  registerForm;
  submitted;
    
  constructor(
    private fb: FormBuilder,
		private authenticationService: AuthenticationService,
		private router: Router,
		private toastr: ToastrService
  ) { 
    this.registerForm = this.fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', Validators.required],
      username: ['', Validators.required],
      password: ['', [Validators.required, Validators.minLength(8)]]
    });
  }


  constructor(private utilityService: UtilityService) { }


  ngOnInit() {
    this.utilityService.resetNavbar();
  }



}
