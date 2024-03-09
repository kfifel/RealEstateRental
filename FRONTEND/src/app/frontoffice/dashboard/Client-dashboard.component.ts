import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {interval, Observable, Subject} from 'rxjs';
import {distinctUntilChanged, map, switchMap} from 'rxjs/internal/operators';
import { OwlOptions } from 'ngx-owl-carousel-o';
import {authUtils} from "../../authUtils";
import {AuthenticationService} from "../../core/services/auth.service";
import {PropertyService} from "../../backoffice/property/service/property.service";
import {IProperty} from "../../backoffice/property/property.model";
import {fileUtils} from "../../core/utils/file.utils";
import {CityService} from "../../core/services/city.service";
import {NgbDate} from "@ng-bootstrap/ng-bootstrap";
import {Router} from "@angular/router";

@Component({
  selector: 'app-frontoffice',
  templateUrl: './Client-dashboard.component.html',
  styleUrls: ['./Client-dashboard.component.scss']
})

/**
 * Crypto landing page
 */
export class ClientDashboardComponent implements OnInit {

  // set the currenr year
  year: number = new Date().getFullYear();
  currentSection = 'home';
  isUserAuth: boolean = false;
  isAdmin: boolean;

  carouselOption: OwlOptions = {
    items: 1,
    loop: false,
    margin: 24,
    nav: false,
    dots: false,
    responsive: {
      672: {
        items: 3
      },
      912: {
        items: 4
      },
    }
  }

  private _trialEndsAt: string;

  private _diff: number;
  _days: number;
  _hours: number;
  _minutes: number;
  _seconds: number;
  property$: Observable<IProperty[]>;

  citySuggestions: string[] = [];

  searchQueries: {startDate: Date, endDate: Date, city: string} = {
    startDate: null,
    endDate: null,
    city: ''
  } ;
  searchInput: Subject<string> = new Subject<string>();

  hoveredDate: NgbDate;
  fromNGDate: NgbDate;
  toNGDate: NgbDate;

  hidden: boolean = true;
  selected: string;

  @Input() fromDate: Date;
  @Input() toDate: Date;
  @ViewChild('dp', { static: true }) datePicker: any;

  constructor(private authService: AuthenticationService,
              private propertyService: PropertyService,
              private cityService: CityService,
              private router: Router) {

  }

  ngOnInit() {
    this._trialEndsAt = "2021-12-31";
    this.setupAutocomplete();

    interval(3000).pipe(
      map(() => {
        this._diff = Date.parse(this._trialEndsAt) - Date.parse(new Date().toString());
      })).subscribe(() => {
        this._days = this.getDays(this._diff);
        this._hours = this.getHours(this._diff);
        this._minutes = this.getMinutes(this._diff);
        this._seconds = this.getSeconds(this._diff);
      });

    this.isUserAuth = authUtils.isAuthenticate();
    this.isAdmin = authUtils.canAccessToBackOffice();

    this.property$ = this.propertyService.query({
      page: 0,
      size: 4,
      sort: ['id,desc'],
    })
      .pipe(
        map((response) => response.body)
    );
  }

  getDays(t) {
    return Math.floor(t / (1000 * 60 * 60 * 24));
  }

  getHours(t) {
    return Math.floor((t / (1000 * 60 * 60)) % 24);
  }

  getMinutes(t) {
    return Math.floor((t / 1000 / 60) % 60);
  }

  getSeconds(t) {
    return Math.floor((t / 1000) % 60);
  }

  /**
   * Window scroll method
   */
  windowScroll() {
    const navbar = document.getElementById('navbar');
    if (document.body.scrollTop >= 50 || document.documentElement.scrollTop >= 50) {
      navbar.classList.add('nav-sticky')
    } else {
      navbar.classList.remove('nav-sticky')
    }
  }

  /**
   * Toggle navbar
   */
  toggleMenu() {
    document.getElementById('topnav-menu-content').classList.toggle('show');
  }

  /**
   * Section changed method
   * @param sectionId specify the current sectionID
   */
  onSectionChange(sectionId: string) {
    this.currentSection = sectionId;
  }

  logout() {
    this.authService.logout()
  }

  readonly fileUtils = fileUtils;

  searchProperty() {

    if(!this.searchQueries.startDate || !this.searchQueries.endDate || !this.searchQueries.city) {
        return;
    }

    const queryParams = {
      city: this.searchQueries.city,
      startDate: this.searchQueries.startDate.toISOString(),
      endDate: this.searchQueries.endDate.toISOString()
    };

    this.router.navigate(['/client/property'], { queryParams });
  }

  setupAutocomplete() {
    this.searchInput.pipe(
      //debounceTime(300),
      distinctUntilChanged(),
      switchMap(value => this.cityService.getSuggestions(value))
    ).subscribe(suggestions => {
      this.citySuggestions = suggestions;
    });
  }

  onCitySelected(city: string) {
    this.searchQueries.city = city;
  }

  onDateSelection(date: NgbDate) {
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

    let dates = this.selected.split('-');
    if(dates.length === 2) {
      this.searchQueries.startDate = this.stringToDate(dates[0]);
      this.searchQueries.endDate = this.stringToDate(dates[1]);
    }
  }

  stringToDate(dateStr: string) {
    const [day, month, year] = dateStr.split('/');
    return new Date(parseInt(year), parseInt(month) , parseInt(day));
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

}
