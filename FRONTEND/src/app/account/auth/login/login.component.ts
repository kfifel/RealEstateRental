import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

import { AuthenticationService } from '../../../core/services/auth.service';

import { OwlOptions } from 'ngx-owl-carousel-o';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
/**
 * Login-2 component
 */
export class LoginComponent implements OnInit {

  constructor(private formBuilder: FormBuilder,
              private route: ActivatedRoute,
              private router: Router,
              private authenticationService: AuthenticationService) { }

  loginForm: FormGroup;
  submitted = false;
  error = '';
  returnUrl: string;
  showPassword: boolean = false;

  // set the currenr year
  year: number = new Date().getFullYear();

  ngOnInit(): void {
    document.body.classList.add('auth-body-bg')
    this.loginForm = this.formBuilder.group({
      email: ['khalid@gmail.com', [Validators.required, Validators.email]],
      password: ['password', [Validators.required]],
    });

    // reset login status
    // this.authenticationService.logout();
    // get return url from route parameters or default to '/'
    // tslint:disable-next-line: no-string-literal
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
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

  get f() { return this.loginForm.controls; }

  /**
   * Form submit
   */
  onSubmit() {
    this.submitted = true;

    // stop here if form is invalid
    if (this.loginForm.invalid) {
      return;
    } else {
      this.authenticationService.login(this.f.email.value, this.f.password.value)
        .subscribe({
          next: () => {
            this.router.navigate(['/dashboard'])
              .then(r => console.log(r))
              .catch(e => console.error(e));
          },
          error: error => {
            this.error = error ? error : '';
          }
        });
    }
  }

  togglePassword() {
    this.showPassword = !this.showPassword;
  }
}
