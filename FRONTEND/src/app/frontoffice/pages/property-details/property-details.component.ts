import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {IProperty} from "../../../backoffice/property/property.model";
import {fileUtils} from "../../../core/utils/file.utils";

@Component({
  selector: 'app-property-details',
  templateUrl: './property-details.component.html',
  styleUrls: ['./property-details.component.scss']
})
export class PropertyDetailsComponent implements OnInit {

  property: IProperty;
  readonly fileUtils = fileUtils;
  breadCrumbItems: any;

  constructor(private activatedRoute: ActivatedRoute) { }

  ngOnInit(): void {
    this.breadCrumbItems = [{ label: 'Property' }, { label: 'Property Details', active: true }];

    this.activatedRoute.data.subscribe((data) =>  {
      this.property = data?.property
    })
  }

}
