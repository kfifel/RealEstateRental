import { Injectable } from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';

import { User } from '../models/auth.models';
import {environment} from "../../../environments/environment";
import {Pagination, SearchWithPagination} from "../request/request.model";
import {Observable} from "rxjs";
import {createRequestOption} from "../request/request.util";
import {Role} from "../models/role.enum";

export type EntityArrayResponseType = Observable<HttpResponse<User[]>>;
export type EntityResponseType = Observable<User>;
@Injectable({ providedIn: 'root' })
export class UserProfileService {
  private resourceUrl = environment.apiUrl + '/api/v1/users';
  constructor(private http: HttpClient) { }

    query(req?: Pagination): EntityArrayResponseType {
        const options = createRequestOption(req);
      return this.http.get<User[]>(this.resourceUrl, {params: options, observe: 'response' });
  }

  search(req: SearchWithPagination): EntityArrayResponseType {
    const options = createRequestOption(req);
    return this.http.get<User[]>(`${this.resourceUrl}`, { params: options, observe: 'response' });
  }

  enableMember(id: number, enable: boolean) {
    return this.http.patch(`${this.resourceUrl}/${id}/${enable}`, {});
  }

  updateRoles(userId: number, roles: Role[]) {
    return this.http.put(`${this.resourceUrl}/${userId}/roles`, roles);
  }
}
