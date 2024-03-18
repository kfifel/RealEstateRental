import {Injectable} from '@angular/core';
import {HttpClient, HttpResponse} from "@angular/common/http";
import {environment} from "../../../../environments/environment";
import {Observable} from "rxjs";
import {IProperty, PropertyModel, PropertySearch} from "../property.model";
import {Pagination, SearchWithPagination} from "../../../core/request/request.model";
import {createRequestOption} from "../../../core/request/request.util";
import {authUtils} from "../../../authUtils";
import {Role} from "../../../core/models/role.enum";


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
    const uri = (authUtils.hasCurrentUserRole(Role.ROLE_PROPERTY) &&
        !authUtils.hasCurrentUserRole(Role.ROLE_ADMIN))
        ? this.resourceUrl + '/my-property'
        : this.resourceUrl;
    const options = createRequestOption(req);
    return this.http.get<IProperty[]>(uri,  { params: options, observe: 'response' });
  }

  available(startDate: string, endDate: string, city: string, req?: Pagination):  Observable<EntityArrayResponseType> {
    let options = createRequestOption({...req, startDate, endDate, city});
    return this.http.get<IProperty[]>(`${this.resourceUrl}/available`,  { params: options, observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    const uri = (authUtils.hasCurrentUserRole(Role.ROLE_PROPERTY) &&
                        !authUtils.hasCurrentUserRole(Role.ROLE_ADMIN))
                      ? this.resourceUrl + '/my-property'
                      : this.resourceUrl;
    return this.http.get<IProperty[]>(uri, { params: options, observe: 'response' });
  }

  deepSearch(propertySearch: PropertySearch, req: Pagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.post<IProperty[]>(`${this.resourceUrl}/search`, propertySearch, { params: options, observe: 'response' });
  }

  top4(): Observable<IProperty[]> {
    return this.http.get<IProperty[]>(`${this.resourceUrl}/top-4`);
  }

  createProperty(formData: FormData): Observable<PropertyModel> {
    return this.http.post<PropertyModel>(this.resourceUrl, formData);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.resourceUrl}/${id}`);
  }
}
