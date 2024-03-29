import { Injectable } from '@angular/core';

import { authUtils } from '../../authUtils';

import { User } from '../models/auth.models';
import {Observable, of} from "rxjs";
import {JwtAuthenticationResponse} from "../../account/auth/jwt-authentication-response.model";
import {HttpClient} from "@angular/common/http";
import {environment} from "../../../environments/environment";
import {map, mergeMap, tap} from "rxjs/operators";
import {Router} from "@angular/router";

@Injectable({ providedIn: 'root' })

export class AuthenticationService {

  private apiUrl: string = environment.apiUrl + "/api/v1/auth/";

  user: User;

  constructor(private http: HttpClient,
              private router: Router) {
  }

  private fetchMe = mergeMap((response: JwtAuthenticationResponse) => {
    // login successful if there's a jwt token in the response
    if (response && response.accessToken && response.refreshToken) {
      // retrieve the user info
      return this.me(response.accessToken).pipe(
        tap((user: User) => {
          authUtils.setLoggedCredentials(user, response);
        }),
        map(() => response)
      );
    } else {
      return of(response);
    }
  });

  /**
   * Registers the user with given details
   */
  register(user: User): Observable<JwtAuthenticationResponse> {
    return this.http.post<JwtAuthenticationResponse>(this.apiUrl + "register", user)
      .pipe(this.fetchMe);
  }

  /**
   * Login user with given details
   */
  login(email: string, password: string): Observable<JwtAuthenticationResponse> {

    return this.http.post<JwtAuthenticationResponse>(this.apiUrl + "login", {email, password})
      .pipe(this.fetchMe);
  }

  me(access_token: string): Observable<User> {
    return this.http.get<User>(this.apiUrl + "me", {headers: {Authorization: `Bearer ${access_token}`}});
  }

  refresh(refresh_token: string): Observable<JwtAuthenticationResponse> {
    return this.http.get<JwtAuthenticationResponse>(this.apiUrl + "token/refresh", {headers: {Authorization: `Bearer ${refresh_token}`}});
  }

  resetPassword(email: string) {
    return this.http.post<void>(this.apiUrl + "password-reset/request", {}, {params: {email}});
  }

  validToken(token: string) {
    return this.http.post<boolean>(this.apiUrl + "password-reset/validate", {}, {params: {token}});
  }

  resetPasswordWithToken(token: string, password: string) {
    return this.http.post<void>(this.apiUrl + "password-reset/reset", {}, {params: {token, newPassword: password}});
  }
/**
   * Logout the user
   */
  logout() {
      // logout the user
      authUtils.logout();
      this.router.navigate(['/account/auth/login']);
  }
}

