import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class PropertyService {

  private apiUrl = environment.apiUrl + "/property";
  constructor(private http: HttpClient) { }

  findById(id: string) {
    return this.http.get(`${this.apiUrl}/${id}`);
  }
}
