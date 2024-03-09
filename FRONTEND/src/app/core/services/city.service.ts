import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {environment} from "../../../environments/environment";
import {Observable, of} from "rxjs";
import {map} from "rxjs/operators";


@Injectable({
  providedIn: 'root'
})
export class CityService {

  private baseUrl = environment.apiUrl + "/api/v1/cities"
  constructor(private http: HttpClient) { }

  getCities(): Observable<string[]> {
    if(localStorage.getItem('cities') !== null) {
      let cities: string[] = JSON.parse(localStorage.getItem('cities'));
      return of(cities);
    }
    return this.http.get<string[]>(this.baseUrl).pipe(
      map(cities => {
        localStorage.setItem('cities', JSON.stringify(cities));
        return cities;
      })
    );
  }

  getSuggestions(value: string): Observable<string[]> {
    value = value?.toLowerCase();
    return this.getCities().pipe(
      map(cities => {
        return cities.filter(city => city.toLowerCase().includes(value))
      })
    )
  }
}
