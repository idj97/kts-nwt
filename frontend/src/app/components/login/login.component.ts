import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import{HomeComponent} from 'src/app/components/home/home.component';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  form: FormGroup;
 

	constructor(
		private fb: FormBuilder,
		private authenticationService: AuthenticationService,
		private router: Router,
		private toastr: ToastrService
	) {
		this.form = this.fb.group({
			username : ['', Validators.required],
			password: ['', Validators.required]
		});
	}

	ngOnInit() {
	}

	submit() {
		const auth: any = {};
		auth.username = this.form.value.username;
		auth.password = this.form.value.password;

		this.authenticationService.login(auth).subscribe(
			result => {
				this.toastr.success('Successful login!');
				localStorage.setItem('user', JSON.stringify(result));
				this.router.navigate(['/home']);
			},
			error => {
				this.toastr.error(error.error);
			}
		);
	}
}
