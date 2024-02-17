import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../../../environments/environment";
import {Observable} from "rxjs";
import {IProperty, PropertyModel} from "../property.model";
import {mergeMap, tap} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class PropertyService {

  private apiUrl = environment.apiUrl + "/api/v1/properties";
  constructor(private http: HttpClient) { }

  findById(id: string) {
    return this.http.get(`${this.apiUrl}/${id}`);
  }

  getAll(): Observable<IProperty[]> {
    return this.http.get<IProperty[]>(this.apiUrl);
  }

  createProperty(property: IProperty, images:FormData): Observable<PropertyModel> {
    return this.http.post<PropertyModel>(this.apiUrl, property).pipe(
      mergeMap((property: PropertyModel) => {
        return this.createPropertyImages(property.id, images);
      })
    )
  }

  private createPropertyImages(propertyId: number, images: FormData): Observable<PropertyModel> {
    return this.http.post<PropertyModel>(`${this.apiUrl}/${propertyId}/images`, images);
  }
}
