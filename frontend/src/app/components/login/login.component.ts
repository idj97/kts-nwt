import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { AuthenticationService } from '../../services/authentication.service';
import { Router } from '@angular/router';
import { ToasterService } from '../../services/toaster.service';
import { HomeComponent } from '../../components/home/home.component';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private authenticationService: AuthenticationService,
    private router: Router,
    private toastr: ToasterService
  ) {
    this.loginForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  ngOnInit() {}

  onSubmit(): void {
    const auth: any = {};
    auth.username = this.loginForm.value.username;
    auth.password = this.loginForm.value.password;

    this.authenticationService.login(auth).subscribe(
      result => {
        this.toastr.showErrorMessage('Successful login!');
        localStorage.setItem('user', JSON.stringify(result));
        this.router.navigate(['/home']);
      },
      error => {
        this.toastr.showMessage(error.error.message, 'Warning');
      }
    );
  }
}
