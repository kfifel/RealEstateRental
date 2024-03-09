import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {IProperty} from "../../../backoffice/property/property.model";
import {fileUtils} from "../../../core/utils/file.utils";
import { NgbDate, NgbCalendar, NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import {map} from "rxjs/operators";

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
  selected: any;

  totalPrice: number;

  @Input() fromDate: Date;
  @Input() toDate: Date;
  @ViewChild('dp', { static: true }) datePicker: any;

  constructor(private activatedRoute: ActivatedRoute) { }

  ngOnInit(): void {
    this.breadCrumbItems = [{ label: 'Property' }, { label: 'Property Details', active: true }];

    this.activatedRoute.data
      .pipe(
        map((data) => data?.property as IProperty),
        map(property => {
          property.images = property.images.map(image => this.fileUtils.getImageUrl(image));
          return property;
        })
      )
      .subscribe((property) =>  {
        this.property = property
      })

    this.selected = '';
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

  reserveProperty() {
    if (this.selected.split('-').length < 2){
      this.hidden = !this.hidden;
      return;
    }
  }
}
