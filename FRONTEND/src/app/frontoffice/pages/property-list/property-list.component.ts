import {Component, HostListener, OnInit} from '@angular/core';
import {fileUtils} from "../../../core/utils/file.utils";
import {BehaviorSubject, Observable} from "rxjs";
import {IProperty, PropertySearch, PropertyType} from "../../../backoffice/property/property.model";
import {PropertyService} from "../../../backoffice/property/service/property.service";
import {catchError, map} from "rxjs/operators";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-property-list',
  templateUrl: './property-list.component.html',
  styleUrls: ['./property-list.component.scss']
})
export class PropertyListComponent implements OnInit {

  readonly fileUtils = fileUtils;
  private timer: ReturnType<typeof setTimeout>;

  breadCrumbItems: any;

  page = 1;
  pageSize = 10;
  loading = false;
  totalItems = 0;

  searchQueries: {startDate: Date, endDate: Date, city: string} = {
    startDate: null,
    endDate: null,
    city: null
  } ;

  deepSearchQueries: PropertySearch = {
    title: null,
    description: null,
    address: null,
    numberOfRooms: null,
    minPricePerDay: null,
    maxPricePerDay: null,
    propertyType: null,
    city: null,
  };

  propertyKeys = Object.keys(PropertyType);

  properties$ = new BehaviorSubject<IProperty[]>([]);

  constructor(private propertyService: PropertyService,
              private activeRoute: ActivatedRoute,
              private router: Router) { }

  ngOnInit(): void {
    this.breadCrumbItems = [{ label: 'Property' }, { label: 'Property Available', active: true }];
    this.activeRoute.queryParams.subscribe(params => {
      this.searchQueries.city = params['city'] ?? null;
      if (params['startDate'])
        this.searchQueries.startDate = new Date(params['startDate']);
      if (params['endDate'])
        this.searchQueries.endDate = new Date(params['endDate']);
    });

    this.loadAvailableProperties()
  }

  @HostListener('window:scroll', ['$event'])
  onScroll() {
    if ((window.innerHeight + window.scrollY) >= document.body.offsetHeight) {
      this.debounce(this.loadMore, 300);
    }
  }

  private debounce(func: Function, delay: number) {
    clearTimeout(this.timer);
    this.timer = setTimeout(() => func.apply(this), delay);
  }

  private loadAvailableProperties() {
    this.loading = true;
    let startDateISOString: string | null = null;
    let endDateISOString: string | null = null;

    if (this.searchQueries.startDate) {
      startDateISOString = this.searchQueries.startDate.toISOString();
    }

    if (this.searchQueries.endDate) {
      endDateISOString = this.searchQueries.endDate.toISOString();
    }
    this.propertyService.available(
      startDateISOString,
      endDateISOString,
      this.searchQueries.city,
    {
        page: this.page - 1,
        size: this.pageSize,
        sort: null
      }
    ).subscribe(
      properties => {
        this.totalItems = Number(properties.headers.get('X-Total-Count')) ?? 0;
        this.properties$.next([...this.properties$.value, ...properties.body]);
        this.loading = false;
      },
      () => {
        this.loading = false;
        return [];
      }
    );
  }

  private loadMore() {
    if (!this.loading && this.totalItems > this.page * this.pageSize) {
      this.page++;
      this.loadAvailableProperties();
    }
  }

  navigateToDetail(id: number) {
    this.router.navigate(['/client/property', id], {queryParams: this.searchQueries});
  }

  deepFilter() {
    this.loading = true;
    this.page = 1;
    this.properties$.next([]);

    this.propertyService.deepSearch(this.deepSearchQueries, {
      page: 0,
      size: this.pageSize,
      sort: null
    }).subscribe(
      (response) => {
        this.totalItems = Number(response.headers.get('X-Total-Count')) ?? 0;
        this.properties$.next(response.body);
        this.loading = false;
      },
      () => {
        this.loading = false;
          return [];
      }
    );
  }
}
