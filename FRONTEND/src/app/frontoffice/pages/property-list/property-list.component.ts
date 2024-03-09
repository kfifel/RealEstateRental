import {Component, HostListener, OnInit} from '@angular/core';
import {fileUtils} from "../../../core/utils/file.utils";
import {BehaviorSubject, Observable} from "rxjs";
import {IProperty} from "../../../backoffice/property/property.model";
import {PropertyService} from "../../../backoffice/property/service/property.service";
import {catchError, map} from "rxjs/operators";
import {ActivatedRoute} from "@angular/router";

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
  pageSize = 5;
  loading = false;
  totalItems = 0;

  searchQueries: {startDate: Date, endDate: Date, city: string} = {
    startDate: null,
    endDate: null,
    city: null
  } ;
  properties$ = new BehaviorSubject<IProperty[]>([]);

  constructor(private propertyService: PropertyService,
              private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.breadCrumbItems = [{ label: 'Property' }, { label: 'Property Details', active: true }];
    this.route.queryParams.subscribe(params => {
      this.searchQueries.city = params['city'] ?? null;
      this.searchQueries.startDate = new Date(params['startDate']) ?? null;
      this.searchQueries.endDate = new Date(params['endDate']) ?? null;
    });

    if (this.searchQueries.city || this.searchQueries.startDate || this.searchQueries.endDate) {
      this.loadAvailableProperties()
    }
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
    this.propertyService.available(
      this.searchQueries.startDate.toISOString(),
      this.searchQueries.endDate.toISOString(),
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
    if (!this.loading && this.totalItems >= (this.page + 1) * this.pageSize) {
      this.page++;
      this.loadAvailableProperties();
    }
  }
}
