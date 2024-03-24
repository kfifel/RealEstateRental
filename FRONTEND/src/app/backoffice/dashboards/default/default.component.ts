import { Component, OnInit, ViewChild } from '@angular/core';
import { ChartType } from './dashboard.model';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { EventService } from '../../../core/services/event.service';

import { ConfigService } from '../../../core/services/config.service';
import {User} from "../../../core/models/auth.models";
import {authUtils} from "../../../authUtils";
import {PropertyService} from "../../property/service/property.service";

@Component({
  selector: 'app-default',
  templateUrl: './default.component.html',
  styleUrls: ['./default.component.scss']
})
export class DefaultComponent implements OnInit {
  user: User = authUtils.getAuthenticatedUser();

  propertiesOverviewChart: ChartType; // For the properties overview chart
  monthlyIncomeChart: ChartType; // For the monthly income chart
  inquiriesSourceChart: ChartType; // New chart for inquiries source
  totalProperties = 100; // Example data
  monthlyRentalIncome = 5000; // Example data
  percentageChange = 10; // Example data
  propertiesData = [ // Example data
    { title: 'Property A', value: 50, icon: 'icon1' },
    { title: 'Property B', value: 30, icon: 'icon2' },
    { title: 'Property C', value: 20, icon: 'icon3' }
  ];
  topCity1 = { name: 'Agadir', rentals: 4 }; // Example data
  isActive: string = 'week'; // Example data

  // Mock data for charts (You should replace this with actual data)
  monthlyRentalIncomeChart = {
    series: [5000, 6000, 7000, 8000, 9000],
    chart: { type: 'line', height: 350 },
    legend: { show: false },
    colors: ['#556ee6'],
    labels: ['Week 1', 'Week 2', 'Week 3', 'Week 4', 'Week 5'],
    stroke: { curve: 'smooth' },
    plotOptions: { bar: { columnWidth: '50%' } }
  };

  propertiesPerformanceChart = {
    chart: { type: 'bar', height: 350 },
    series: [{ name: 'Properties', data: [30, 40, 45, 50, 49, 60, 70, 91, 125] }],
    legend: { show: false },
    colors: ['#34c38f'],
    fill: { type: 'gradient', gradient: { shade: 'light', type: 'horizontal', shadeIntensity: 0.5, gradientToColors: ['#8fce61'], inverseColors: false, opacityFrom: 1, opacityTo: 1, stops: [0, 100] } },
    dataLabels: { enabled: false },
    xaxis: { categories: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'] },
    plotOptions: { bar: { horizontal: false, endingShape: 'rounded', columnWidth: '55%' } }
  };

  constructor(
    private modalService: NgbModal,
    private configService: ConfigService,
    private eventService: EventService,
    private propertyService: PropertyService // Assuming you have a service to fetch property-related data
  ) {}

  ngOnInit() {
    this.fetchData();
  }

  private fetchData() {
    // Example of fetching data for charts, adjust according to your real service methods
    this.propertyService.getPropertiesOverview().subscribe(data => {
      this.propertiesOverviewChart = data;
    });

    this.propertyService.getMonthlyIncome().subscribe(data => {
      this.monthlyIncomeChart = data;
    });

    this.propertyService.getInquiriesSource().subscribe(data => {
      this.inquiriesSourceChart = data;
    });

    // You might want to set the default view or get it from user preferences
    this.isActive = 'year';
  }

  weeklyreport() {
    // Update chart data for weekly report
    // Adjust implementation based on your actual data fetching logic
  }

  yearlyreport() {
    // Update chart data for yearly report
    // Adjust implementation based on your actual data fetching logic
  }

  changeLayout(layout: string) {
    this.eventService.broadcast('changeLayout', layout);
  }

  weeklyPerformance() {
    this.isActive = 'week';
    // Logic to update chart data for weekly performance
  }

  monthlyPerformance() {
    this.isActive = 'month';
    // Logic to update chart data for monthly performance
  }

  yearlyPerformance() {
    this.isActive = 'year';
    // Logic to update chart data for yearly performance
  }
}
