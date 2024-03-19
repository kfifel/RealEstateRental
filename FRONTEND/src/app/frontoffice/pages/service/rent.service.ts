import { Injectable } from '@angular/core';
import {HttpClient, HttpResponse} from "@angular/common/http";
import {environment} from "../../../../environments/environment";
import {Observable} from "rxjs";
import {IRent} from "../../../core/models/rent.model";
import {Pagination, SearchWithPagination} from "../../../core/request/request.model";
import {createRequestOption} from "../../../core/request/request.util";
import {IProperty} from "../../../backoffice/property/property.model";

type EntityArrayResponseType = Observable<HttpResponse<IRent[]>>;
@Injectable({
  providedIn: 'root'
})
export class RentService {

  resourceUrl = environment.apiUrl + '/api/v1/rents';

  constructor(private http: HttpClient) { }


  reserveProperty(id: number, startDate: Date, endDate: Date): Observable<IRent> {
    return this.http.post<IRent>(`${this.resourceUrl}`, {propertyId: id, startDate, endDate});
  }


  findPropertyRents(propertyId: number, req?: Pagination): EntityArrayResponseType {
    const options = createRequestOption(req);
    return this.http.get<IRent[]>(`${this.resourceUrl}/property/${propertyId}/rent-list`,  { params: options, observe: 'response' });
  }

  search(propertyId: number, req: SearchWithPagination): EntityArrayResponseType {
    const options = createRequestOption(req);
    return this.http.get<IRent[]>(`${this.resourceUrl}/property/${propertyId}/search`, { params: options, observe: 'response' });
  }
}
