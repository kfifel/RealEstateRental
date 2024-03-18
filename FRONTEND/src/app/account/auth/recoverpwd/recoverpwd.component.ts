import { Component, OnInit } from '@angular/core';
import { OwlOptions } from 'ngx-owl-carousel-o';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';

import { AuthenticationService } from '../../../core/services/auth.service';
import { environment } from '../../../../environments/environment';

@Component({
  selector: 'app-recoverpwd',
  templateUrl: './recoverpwd.component.html',
  styleUrls: ['./recoverpwd.component.scss']
})
export class RecoverpwdComponent implements OnInit {

  // set the currenr year
  year: number = new Date().getFullYear();

  resetForm: FormGroup;
  resetPwdForm: FormGroup;
  submitted = false;
  error = '';
  success = '';
  loading = false;
  isTokenValid: boolean = false;
  _token: string = null;
  showPassword = false;

  constructor(private formBuilder: FormBuilder,
             private route: ActivatedRoute,
             private router: Router,
             private authenticationService: AuthenticationService) { }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this._token = params['token'] ?? null;
    });
    if (this._token) {
      this.authenticationService.validToken(this._token).subscribe(
        data => {
          this.isTokenValid = data;
          if (!this.isTokenValid) {
            this.error = 'Invalid token or expired. Please try again.';
          }
        },
        error => {
          this.error = error ? error : '';
        }
      );
    }
    this.resetForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
    });

    this.resetPwdForm = this.formBuilder.group({
      password: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', [Validators.required, Validators.minLength(6)]],
    });
  }

  // convenience getter for easy access to form fields
  get f() { return this.resetForm.controls; }
  get pwdForm() { return this.resetPwdForm.controls; }

  /**
   * On submit form
   */
  onSubmit() {
    this.success = '';
    this.error = '';
    this.submitted = true;

    // stop here if form is invalid
    if (this.resetForm.invalid) {
      return;
    }

    this.authenticationService.resetPassword(this.f.email.value).subscribe(
      data => {
        this.success = 'Password reset link has been sent to your email.';
      },
      error => {
        this.error = 'Mail not found. Please enter a valid email.';
      }
    )
  }

  carouselOption: OwlOptions = {
    items: 1,
    loop: false,
    margin: 0,
    nav: false,
    dots: true,
    responsive: {
      680: {
        items: 1
      },
    }
  }

  onResetPwd() {
    if (this.resetPwdForm.invalid) {
      return;
    }
    if (this.pwdForm.password.value !== this.pwdForm.confirmPassword.value) {
      this.error = 'Password not match.';
      return;
    }
    this.loading = true;
    this.error = '';
    this.success = '';
    this.authenticationService.resetPasswordWithToken(this._token, this.pwdForm.password.value).subscribe(
      data => {
        this.loading = false;
        this.success = 'Password reset successfully.';
      },
      error => {
        this.loading = false;
        this.error = error ? error : '';
      }
    );
  }
}
