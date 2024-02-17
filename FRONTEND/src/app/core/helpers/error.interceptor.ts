import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { AuthenticationService } from '../services/auth.service';
import {authUtils} from "../../authUtils";

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
    constructor(private authenticationService: AuthenticationService) { }

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

        return next.handle(request).pipe(catchError(err => {
            if (err.status === 401) {
                // auto logout if 401 response returned from api
              this.refreshToken();
            }
            console.error(err);
            const error = err.error?.message || err?.statusText || "An error occurred. Please try again later.";
            return throwError(error);
        }));
    }

    refreshToken() {

      let refresh_token = authUtils.getRefreshToken();
      authUtils.removeAccessToken();
      this.authenticationService.refresh(refresh_token).subscribe(
        (response) => {
          if (response && response.accessToken && response.refreshToken) {
            // retrieve the user info
            this.authenticationService.me(response.accessToken).subscribe((user) => {
              authUtils.setLoggedCredentials(user, response);
              location.reload();
            });
          } else {
            this.authenticationService.logout();
            location.reload();
          }
        },
        (error) => {
          this.authenticationService.logout();
          location.reload();
        }
      );
    }
}
