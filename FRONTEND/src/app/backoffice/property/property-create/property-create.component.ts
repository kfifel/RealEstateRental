import {Component, OnInit} from '@angular/core';
import {PropertyModel, PropertyType} from "../property.model";
import * as ClassicEditor from '@ckeditor/ckeditor5-build-classic';
import {Observable} from "rxjs";
import {CityService} from "../../../core/services/city.service";
import {PropertyService} from "../service/property.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-property-create',
  templateUrl: './property-create.component.html',
  styleUrls: ['./property-create.component.scss']
})
export class PropertyCreateComponent implements OnInit {
  // bread crumb items
  breadCrumbItems: Array<{}>;
  files: File[] = [];
  propertyKeys = Object.keys(PropertyType);
  property: PropertyModel = {
    title: ' ',
    description: '',
    pricePerMonth: 0,
    pricePerDay: null,
    images: null,
    propertyType: PropertyType.APARTMENT,
    address: "",
    city: "",
    size: null,
    numberOfRooms: 0,
    hasBalcony: false,
  };
  public Editor = ClassicEditor;
  description: any = "Hello world";
  cities$: Observable<string[]>;
  isLoading = false;

  constructor(private cityService: CityService,
              private propertyService: PropertyService,
              private router: Router) { }

  ngOnInit() {
    this.breadCrumbItems = [{ label: 'Property', link: '/admin/property'  }, { label: 'Property new', active: true }];

    this.cities$ = this.cityService.getCities();
  }

  onSelect(event) {
    this.files.push(...event.addedFiles);

  }

  onRemove(event) {
    this.files.splice(this.files.indexOf(event), 1);
  }

  onContentDescriptionChange(event: any) {
    this.property.description = event.editor.getData();
  }

  onsubmit() {
    this.isLoading = true;
    let images = new FormData();
    this.files.forEach((file) => {
      images.append(`images`, file);
    });

    this.propertyService.createProperty(this.property, images)
      .subscribe(() => {
        this.isLoading = false;
        this.router.navigate(['/admin/property'])
      },
      () => {
        this.isLoading = false;
      });

  }
}
