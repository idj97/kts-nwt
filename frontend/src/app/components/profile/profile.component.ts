import { Component, OnInit } from '@angular/core';
import {UserService} from 'src/app/services/user.service';
import { FormBuilder,FormGroup, Validators } from '@angular/forms';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { Router } from '@angular/router';
import { ToasterService } from 'src/app/services/toaster.service';
import { HttpClient } from '@angular/common/http';
import {User} from 'src/app/models/user'
@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  passwordForm: FormGroup;
  profileForm:FormGroup;

  constructor(
    private fb: FormBuilder,
		private authenticationService: AuthenticationService,
		private router: Router,
    private toastr: ToasterService,
    private userService: UserService,
    private http: HttpClient){
      this.passwordForm = this.fb.group({
        'oldPassword': ['', Validators.required],
        'newPassword': ['', Validators.required,Validators.minLength(5),
        Validators.maxLength(30)],
        
      });
    this.profileForm = this.fb.group({
      'firstname': ['', Validators.required],
      'lastname': ['', Validators.required],
      'email': ['', [Validators.required, Validators.email]]
		});
	}


  ngOnInit(
    
  ) {
    
  }

}
