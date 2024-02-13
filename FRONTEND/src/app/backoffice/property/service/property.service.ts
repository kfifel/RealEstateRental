import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../../../environments/environment";
import {Observable} from "rxjs";
import {IProperty} from "../property.model";
import {tap} from "rxjs/operators";

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
    return this.http.get<IProperty[]>(this.apiUrl).pipe(
      tap((res: any) => {
        console.log(res)
      })
    );
  }
}
