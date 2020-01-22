import { Component, OnInit } from '@angular/core';
import {UserService} from 'src/app/services/user.service';
import { FormBuilder,FormGroup, Validators,FormControl } from '@angular/forms';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { Router } from '@angular/router';
import { ToasterService } from 'src/app/services/toaster.service';
import { HttpClient } from '@angular/common/http';
import {User} from 'src/app/models/user'
import { PasswordEdit } from 'src/app/models/password-edit.model';
import { EditProfileInfo } from 'src/app/models/profile-edit-info.model';


@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
 
  public user:User;
  submitted = false;
  
 
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
		});
	}

  ngOnInit() {
    this.getUserData();
    
  }
  private getUserData() {
  this.userService.getProfileData().subscribe(data => {
    this.user = {
      id: data.id,
      email: data.email,
      username: data.username,
      firstname: data.firstname,
      lastname: data.lastname,
      password:data.password
    };

    this.addValuesToForm();
  }, error => {
    this.toastr.showMessage(error.error.message,'Error');
  })
}


private addValuesToForm(): void {
  this.profileForm.setValue({
    firstname: this.user.firstname,
    lastname: this.user.lastname,
  
  });
}



  

  onSubmit(): void {
    const data: EditProfileInfo = {
      firstname: this.profileForm.controls['firstname'].value,
      lastname: this.profileForm.controls['lastname'].value,
      
    };

    this.userService.changeMyInfo(data).subscribe(data => {
      this.toastr.showErrorMessage('Your profile has been changed.');
    }, error => {
      this.toastr.showMessage(error.error.message,'Error');

    });
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

    this.userService.changePassword(passwords).subscribe(data => {

          this.toastr.showErrorMessage('Your password has been changed. Login again.');
          this.authenticationService.logout();
          this.router.navigate(['/login']);
        },
        error => {
          this.toastr.showMessage(error.error.message,'Error');
          this.submitted = false;
        });

    
		

	}


    
  } 


  