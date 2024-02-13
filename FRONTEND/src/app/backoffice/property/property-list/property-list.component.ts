import { Component, OnInit } from '@angular/core';
import {IProperty} from "../property.model";
import {PropertyService} from "../service/property.service";

@Component({
  selector: 'app-property-list',
  templateUrl: './property-list.component.html',
  styleUrls: ['./property-list.component.scss']
})
export class PropertyListComponent implements OnInit {

  property: IProperty[];
  // bread crumb items
  breadCrumbItems: Array<{}>;

  constructor(private propertyService: PropertyService) { }

  ngOnInit(): void {
    this.breadCrumbItems = [{ label: 'Properties' }, { label: 'Property List', active: true }];

    this.loadProperties();
  }

  loadProperties() {
    this.propertyService.getAll().subscribe(data => {
      this.property = data;
    });
  }
}