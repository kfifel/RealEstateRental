import { Component, OnInit } from '@angular/core';
import {IProperty} from "../property.model";
import {PropertyService} from "../service/property.service";
import {HttpHeaders, HttpResponse} from "@angular/common/http";
import {BehaviorSubject, Observable, Subject} from "rxjs";

@Component({
  selector: 'app-property-list',
  templateUrl: './property-list.component.html',
  styleUrls: ['./property-list.component.scss']
})
export class PropertyListComponent implements OnInit {

  properties: IProperty[] = [];
  // bread crumb items
  breadCrumbItems: Array<{}>;

  currentSearch: string;
  private searchSubject: Subject<string> = new Subject<string>();
  isLoading = false;

  totalItems$ = new BehaviorSubject<number>(0);
  itemsPerPage = 5;
  page: number = 1;

  constructor(private propertyService: PropertyService) { }

  ngOnInit(): void {
    this.breadCrumbItems = [{ label: 'Properties', link: '/admin/property' }, { label: 'Property List', active: true }];

    this.loadAll();

  }

  loadAll(page?: number) {
    const pageToLoad: number = page ?? this.page ?? 1;
    this.isLoading = true;
    let requestObservable$: Observable<any>;
    if (this.currentSearch) {
      requestObservable$ = this.propertyService
        .search({
          page: pageToLoad - 1,
          query: this.currentSearch,
          size: this.itemsPerPage,
          sort: ['id,desc'],
        });
    } else {
      requestObservable$ = this.propertyService
        .query({
          page: pageToLoad - 1,
          size: this.itemsPerPage,
          sort: ['id,desc'],
        })
    }

    requestObservable$
      .subscribe({
        next: (res: HttpResponse<IProperty[]>) => {
          this.isLoading = false;
          this.onSuccess(res.body, res.headers, pageToLoad);
        },
        error: () => {
          this.isLoading = false;
          this.onError();
        },
      });
  }

  handleSearch() {
    this.loadAll(1);
  }

  // Call this method when the search input changes
  onSearchChange(): void {
    this.searchSubject.next(this.currentSearch);
  }

  private onSuccess(body: IProperty[], headers: HttpHeaders, pageToLoad: number) {
    this.isLoading = false;
    const totalCount = Number(headers.get('X-Total-Count'));
    this.totalItems$.next(totalCount);
    this.page = pageToLoad;
    this.properties = body;
  }

  private onError() {
    console.error("Error loading properties.")
  }

  base64ToFile(base64: string) {
    if (!base64)
      return '#';
    return 'data:image/jpeg;base64,' + base64;
  }

  delete(id: number): void {

    this.propertyService.delete(id).subscribe(
  () => {
        this.loadAll();
      }
    );

  }
}
