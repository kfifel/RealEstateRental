import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from '@angular/router';
import {Observable} from 'rxjs';
import {authUtils} from "../../authUtils";
import {Role} from "../models/role.enum";

@Injectable({
  providedIn: 'root'
})
export class AdminAccessGuard implements CanActivate {

  constructor(private router: Router) {
  }

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

    let isAdmin = authUtils.hasCurrentUserRole(Role.ROLE_ADMIN);

    if(!isAdmin) {
      return this.router.createUrlTree(['/']);
    }
    return isAdmin
  }

}
