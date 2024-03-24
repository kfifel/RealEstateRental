import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, RouterStateSnapshot, UrlTree} from '@angular/router';
import {Observable} from 'rxjs';
import {authUtils} from "../../authUtils";
import {Role} from "../models/role.enum";

@Injectable({
  providedIn: 'root'
})
export class PropertyGuard implements CanActivate {
  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    return authUtils.hasCurrentUserRole(Role.ROLE_PROPERTY);
  }

}
