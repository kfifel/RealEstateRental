import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from '@angular/router';
import {Observable} from 'rxjs';
import {authUtils} from "../../authUtils";
import {Role} from "../models/role.enum";

@Injectable({
  providedIn: 'root'
})
export class BackOfficeGuard implements CanActivate {

  constructor(private router: Router) {
  }

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

    let canAssess = authUtils.hasAnyRole([Role.ROLE_PROPERTY, Role.ROLE_ADMIN]);

    if(!canAssess) {
      return this.router.createUrlTree(['/']);
    }
    return canAssess;
  }

}
