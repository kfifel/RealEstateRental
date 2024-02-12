import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {IProperty} from "../property.model";

@Component({
  selector: 'app-property-list',
  templateUrl: './property-list.component.html',
  styleUrls: ['./property-list.component.scss']
})
export class PropertyListComponent implements OnInit {

  property: IProperty;

  constructor(private route: ActivatedRoute) { }

  ngOnInit(): void {

    this.property = this.route.snapshot.data['property'];
  }

}
