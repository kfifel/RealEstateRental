import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {IProperty} from "../property.model";

@Component({
  selector: 'app-property-detail',
  templateUrl: './property-detail.component.html',
  styleUrls: ['./property-detail.component.scss']
})
export class PropertyDetailComponent implements OnInit {

  // bread crumb items
  breadCrumbItems: Array<{}>;
  property: IProperty = {} as IProperty;

  constructor(private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.breadCrumbItems = [{ label: 'Property', link: '/admin/property'  }, { label: 'Property Details', active: true }];

    this.route.data.subscribe(data => {
      console.log(data.property);
      this.property = data.property;
    });
  }

  getImageUrl(image: File): string {
      return 'data:image/jpeg;base64,' + image;
  }

}
