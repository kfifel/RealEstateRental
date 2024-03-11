import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../../../environments/environment";
import {Observable} from "rxjs";
import {IRent} from "../../../core/models/rent.model";

@Injectable({
  providedIn: 'root'
})
export class RentService {

  resourceUrl = environment.apiUrl + '/api/v1/rents';

  constructor(private http: HttpClient) { }


  reserveProperty(id: number, startDate: Date, endDate: Date): Observable<IRent> {
    return this.http.post<IRent>(`${this.resourceUrl}`, {propertyId: id, startDate, endDate});
  }
}
