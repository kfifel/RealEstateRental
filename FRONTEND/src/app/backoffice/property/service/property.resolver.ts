import { Injectable } from '@angular/core';
import {
  Router, Resolve,
  RouterStateSnapshot,
  ActivatedRouteSnapshot
} from '@angular/router';
import { Observable, of } from 'rxjs';
import {IProperty} from "../property.model";
import {PropertyService} from "./property.service";
import {catchError} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class PropertyResolver implements Resolve<IProperty> {

  constructor(private propertyService: PropertyService,
              private router: Router) {
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IProperty> {
    const id = route.paramMap.get('id');
    return this.propertyService.findById(id).pipe(
      catchError(error => {
        console.error('Error loading property:', error);
        this.router.navigate(['/admin/property']); // Navigate back to the previous route
        return of(null); // Return a safe value in case of error
      }),
    );
  }
}
