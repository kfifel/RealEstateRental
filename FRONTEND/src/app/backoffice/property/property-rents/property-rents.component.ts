import {Component, Input, OnInit} from '@angular/core';
import {IRent, RentStatistics} from "../property.model";
import {BehaviorSubject, Observable, Subject} from "rxjs";
import {PropertyService} from "../service/property.service";
import {HttpHeaders, HttpResponse} from "@angular/common/http";
import {RentService} from "../../../frontoffice/pages/service/rent.service";
import {RentStatus} from "../../../core/models/rent.model";
import {SweetAlertService} from "../../../core/services/sweet-alert.service";
import Swal from "sweetalert2";

@Component({
  selector: 'app-property-rents',
  templateUrl: './property-rents.component.html',
  styleUrls: ['./property-rents.component.scss']
})
export class PropertyRentsComponent implements OnInit {

  @Input() propertyId: number;
  rents: IRent[] = [];
  statistics?: RentStatistics = undefined;
  // bread crumb items
  breadCrumbItems: Array<{}>;

  currentSearch: string;
  private searchSubject: Subject<string> = new Subject<string>();
  isLoading = false;

  totalItems$ = new BehaviorSubject<number>(0);
  itemsPerPage = 5;
  page: number = 1;

  constructor(private rentService: RentService,
              private alertService: SweetAlertService) { }

  ngOnInit(): void {
    this.breadCrumbItems = [{ label: 'Properties', link: '/admin/property' }, { label: 'Rents List', active: true }];

    this.loadAll();
    this.loadStatistics()
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

  private loadStatistics() {
    this.rentService.getStatistics(this.propertyId)
      .subscribe((statistics) => {
      this.statistics = statistics;
    })
  }

  handleChangeStatus(id: number, status: RentStatus) {
    if (status !== RentStatus.PENDING)
        return;

    Swal.fire({
      title: 'Choose an action',
      icon: 'question',
      showCancelButton: true,
      cancelButtonText: 'Cancel',
      confirmButtonText: 'Approve',
      confirmButtonColor: '#3085d6',
      showDenyButton: true,
      denyButtonText: 'Reject',
      denyButtonColor: '#d33',
    }).then((result) => {
      if (result.isConfirmed) {
        this.changeStatus(id, RentStatus.APPROVED);
      } else if (result.isDenied) {
        this.changeStatus(id, RentStatus.REJECTED);
      }
      // If result.isDismissed, then the user clicked outside the alert or pressed the Cancel button
    });
  }

  changeStatus(id: number, newStatus: RentStatus) {
    this.rentService.changeStatus(id, newStatus)
      .subscribe(
        (ent) => {
          if (newStatus === RentStatus.APPROVED)
            this.statistics.totalApprovedRent++;
          else if (newStatus === RentStatus.REJECTED)
              this.statistics.totalRejectedRent++;

          this.alertService.success('Success', 'Status changed successfully');
          this.updateRentStatus(id, newStatus);
        });
  }

  private updateRentStatus(id: number, newStatus: RentStatus) {
    const rent = this.rents.find((r) => r.id === id);
    if (rent) {
      rent.status = newStatus;
    }
  }
}
