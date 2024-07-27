import {Injectable} from '@angular/core';
import {HttpClient, HttpResponse} from "@angular/common/http";
import {environment} from "../../../../environments/environment";
import {Observable} from "rxjs";
import {IProperty, PropertyModel, PropertySearch} from "../property.model";
import {Pagination, SearchWithPagination} from "../../../core/request/request.model";
import {createRequestOption} from "../../../core/request/request.util";
import {authUtils} from "../../../authUtils";
import {Role} from "../../../core/models/role.enum";
import {map} from "rxjs/internal/operators";


export type EntityResponseType = HttpResponse<IProperty>;
export type EntityArrayResponseType = HttpResponse<IProperty[]>;

export const updateImagesPath = map((properties: IProperty[]) =>
  properties.map(updateImagePath)
);

export const updateImagePath = (property: IProperty) => {
  const apiUrl = environment.apiUrl;
    property.images = property.images.map(image => {
      image.path = `${apiUrl}/api/v1/properties/images/${image.name}`;
      return image;
    });
    return property;
};

@Injectable({ providedIn: 'root' })
export class PropertyService {

  private resourceUrl = environment.apiUrl + "/api/v1/properties";
  constructor(private http: HttpClient) { }

  findById(id: string) {
    return this.http.get(`${this.resourceUrl}/${id}`)
      .pipe(map(updateImagePath));
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
    return this.http.get<IProperty[]>(`${this.resourceUrl}/available`,  { params: options, observe: 'response' })
      .pipe();
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
    return this.http.post<IProperty[]>(`${this.resourceUrl}/search`, propertySearch,
      { params: options, observe: 'response' })
      .pipe();
  }

  top4(): Observable<IProperty[]> {
    return this.http.get<IProperty[]>(`${this.resourceUrl}/top-4`)
      .pipe(updateImagesPath);
  }

  createProperty(formData: FormData): Observable<PropertyModel> {
    return this.http.post<PropertyModel>(this.resourceUrl, formData).pipe(map(updateImagePath));
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.resourceUrl}/${id}`);
  }

  getPropertiesOverview(): Observable<any> {
    return this.http.get<any>(`${this.resourceUrl}/overview`);
  }

  getMonthlyIncome(): Observable<any> {
    return this.http.get<any>(`${this.resourceUrl}/monthly-income`);
  }

  getInquiriesSource(): Observable<any> {
    return this.http.get<any>(`${this.resourceUrl}/inquiries-source`);
  }
}
