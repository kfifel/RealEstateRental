import {Component, Input, OnInit} from '@angular/core';
import {IRent} from "../property.model";
import {BehaviorSubject, Observable, Subject} from "rxjs";
import {PropertyService} from "../service/property.service";
import {HttpHeaders, HttpResponse} from "@angular/common/http";
import {RentService} from "../../../frontoffice/pages/service/rent.service";

@Component({
  selector: 'app-property-rents',
  templateUrl: './property-rents.component.html',
  styleUrls: ['./property-rents.component.scss']
})
export class PropertyRentsComponent implements OnInit {

  @Input() propertyId: number;
  rents: IRent[] = [];
  // bread crumb items
  breadCrumbItems: Array<{}>;

  currentSearch: string;
  private searchSubject: Subject<string> = new Subject<string>();
  isLoading = false;

  totalItems$ = new BehaviorSubject<number>(0);
  itemsPerPage = 5;
  page: number = 1;

  constructor(private rentService: RentService) { }

  ngOnInit(): void {
    this.breadCrumbItems = [{ label: 'Properties', link: '/admin/property' }, { label: 'Rents List', active: true }];

    this.loadAll();

  }

  loadAll(page?: number) {
    const pageToLoad: number = page ?? this.page ?? 1;
    this.isLoading = true;
    let requestObservable$: Observable<HttpResponse<IRent[]>>;
    if (this.currentSearch) {
      requestObservable$ = this.rentService
        .search(this.propertyId, {
          page: pageToLoad - 1,
          query: this.currentSearch,
          size: this.itemsPerPage,
          sort: ['id,desc'],
        });
    } else {
      requestObservable$ = this.rentService
        .findPropertyRents(this.propertyId, {
          page: pageToLoad - 1,
          size: this.itemsPerPage,
          sort: ['id,desc'],
        })
    }

    requestObservable$
      .subscribe({
        next: (res: HttpResponse<IRent[]>) => {
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

  private onSuccess(body: IRent[], headers: HttpHeaders, pageToLoad: number) {
    this.isLoading = false;
    const totalCount = Number(headers.get('X-Total-Count'));
    this.totalItems$.next(totalCount);
    this.page = pageToLoad;
    this.rents = body;
  }

  private onError() {
    console.error("Error loading properties.")
  }

}
