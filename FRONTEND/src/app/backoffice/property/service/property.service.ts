import { Injectable } from '@angular/core';
import {HttpClient, HttpResponse} from "@angular/common/http";
import {environment} from "../../../../environments/environment";
import {Observable} from "rxjs";
import {IProperty, PropertyModel} from "../property.model";
import {mergeMap} from "rxjs/operators";
import {Pagination, SearchWithPagination} from "../../../core/request/request.model";
import {createRequestOption} from "../../../core/request/request.util";


export type EntityResponseType = HttpResponse<IProperty>;
export type EntityArrayResponseType = HttpResponse<IProperty[]>;

@Injectable({ providedIn: 'root' })
export class PropertyService {

  private resourceUrl = environment.apiUrl + "/api/v1/properties";
  constructor(private http: HttpClient) { }

  findById(id: string) {
    return this.http.get(`${this.resourceUrl}/${id}`);
  }

  query(req?: Pagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IProperty[]>(this.resourceUrl,  { params: options, observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IProperty[]>(`${this.resourceUrl}`, { params: options, observe: 'response' });
  }

  createProperty(property: IProperty, images:FormData): Observable<PropertyModel> {
    return this.http.post<PropertyModel>(this.resourceUrl, property).pipe(
      mergeMap((property: PropertyModel) => {
        return this.createPropertyImages(property.id, images);
      })
    )
  }

  private createPropertyImages(propertyId: number, images: FormData): Observable<PropertyModel> {
    return this.http.post<PropertyModel>(`${this.resourceUrl}/${propertyId}/images`, images);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.resourceUrl}/${id}`);
  }
}
