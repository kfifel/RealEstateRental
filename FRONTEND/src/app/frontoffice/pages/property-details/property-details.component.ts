import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {IProperty} from "../../../backoffice/property/property.model";
import {fileUtils} from "../../../core/utils/file.utils";
import {NgbDate, NgbCalendar, NgbDateStruct, NgbAlert, NgbAlertModule} from '@ng-bootstrap/ng-bootstrap';
import {map} from "rxjs/operators";
import {PropertyService} from "../../../backoffice/property/service/property.service";
import {RentService} from "../service/rent.service";
import * as sweetalert2 from "sweetalert2";

@Component({
  selector: 'app-property-details',
  templateUrl: './property-details.component.html',
  styleUrls: ['./property-details.component.scss']
})
export class PropertyDetailsComponent implements OnInit {

  property: IProperty;
  readonly fileUtils = fileUtils;
  breadCrumbItems: any;

  hoveredDate: NgbDate;
  fromNGDate: NgbDate;
  toNGDate: NgbDate;

  hidden: boolean;
  selected: string = '';

  totalPrice: number;

  @Input() fromDate: Date;
  @Input() toDate: Date;
  @ViewChild('dp', { static: true }) datePicker: any;

  errors = {
    startDate: null,
    endDate: null,
    message: null
  }

  isReserved: boolean = false;

  constructor(private activatedRoute: ActivatedRoute,
              private rentService: RentService) { }

  ngOnInit(): void {
    this.breadCrumbItems = [{ label: 'Property' }, { label: 'Property Details', active: true }];

    this.activatedRoute.data
      .pipe(
        map((data) => data?.property as IProperty),
        map(property => {
          property.images = property.images.map(image => {
            return {
              id : image.id,
              base64: this.fileUtils.getImageUrl(image.base64)
            }
          });
          return property;
        })
      )
      .subscribe((property) =>  {
        this.property = property
      })

    this.activatedRoute.queryParams.subscribe(params => {
      const param1 = params['startDate'];
      const param2 = params['endDate'];
      if (param2 && param1) {
        let startDate = new Date(param1);
        let endDate = new Date(param2);
        this.selected = this.dateToString(startDate) + '-' + this.dateToString(endDate);
        this.calculateTotalPrice();
      }
    })
    this.hidden = true;
  }

  isHovered(date: NgbDate) {
    return this.fromNGDate && !this.toNGDate && this.hoveredDate && date.after(this.fromNGDate) && date.before(this.hoveredDate);
  }

  /**
   * @param date date obj
   */
  isInside(date: NgbDate) {
    return date.after(this.fromNGDate) && date.before(this.toNGDate);
  }

  /**
   * @param date date obj
   */
  isRange(date: NgbDate) {
    return date.equals(this.fromNGDate) || date.equals(this.toNGDate) || this.isInside(date) || this.isHovered(date);
  }

  public onDateSelection(date: NgbDate) {
    if (!this.fromDate && !this.toDate) {
      this.fromNGDate = date;
      this.fromDate = new Date(date.year, date.month - 1, date.day);
      this.selected = '';
    } else if (this.fromDate && !this.toDate && date.after(this.fromNGDate)) {
      this.toNGDate = date;
      this.toDate = new Date(date.year, date.month - 1, date.day);
      this.hidden = true;
      this.selected = this.fromDate.toLocaleDateString() + '-' + this.toDate.toLocaleDateString();

      this.fromDate = null;
      this.toDate = null;
      this.fromNGDate = null;
      this.toNGDate = null;

    } else {
      this.fromNGDate = date;
      this.fromDate = new Date(date.year, date.month - 1, date.day);
      this.selected = '';
    }
    this.calculateTotalPrice();
  }

  private calculateTotalPrice() {
    if(this.selected.split('-').length < 2)
      return;

    let [startDateStr, endDateStr] = this.selected.split('-');
    const startDate = this.stringToDate(startDateStr);
    const endDate = this.stringToDate(endDateStr);
    const days = this.calculateDays(startDate, endDate);
    let totalPrice = 0;
    if ( days > 30)
      totalPrice = this.property.pricePerMonth * (days / 30);
    else
      totalPrice = this.property.pricePerDay * days;

    // 0.354 -> 0.36
    this.totalPrice = Math.round(totalPrice * 100) / 100;

  }

  private calculateDays(start: Date, end: Date) {
    const differenceMs = end.getTime() -  start.getTime();
    return Math.ceil(differenceMs / (1000 * 60 * 60 * 24));
  }

  stringToDate(dateStr: string) {
    const [day, month, year] = dateStr.split('/');
    return new Date(parseInt(year), parseInt(month) , parseInt(day));
  }

  dateToString(date: Date) {
    return date.getDate() + '/' + date.getMonth() + '/' + date.getFullYear();
  }

  reserveProperty() {
    this.errors.message = null;
    if (this.selected.split('-').length < 2){
      this.hidden = !this.hidden;
      return;
    }

    if (this.errors.startDate || this.errors.endDate)
      return;

    const [startDateStr, endDateStr] = this.selected.split('-');
    const startDate = this.stringToDate(startDateStr);
    const endDate = this.stringToDate(endDateStr);

    this.isReserved = true;
    this.rentService.reserveProperty(this.property.id, startDate, endDate)
        .subscribe(() => {
            sweetalert2.default.fire('Success', 'Property reserved successfully', 'success');
        },
          (error) => {
            this.errors.message = error.error.message ?? 'Error has been occurred';
            this.isReserved = false;
          })

  }
}
