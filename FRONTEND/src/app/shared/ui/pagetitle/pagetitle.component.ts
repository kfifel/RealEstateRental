import { Component, OnInit, Input } from '@angular/core';
import {BreadCrumbItem} from "./pagetitle.model";

@Component({
  selector: 'app-page-title',
  templateUrl: './pagetitle.component.html',
  styleUrls: ['./pagetitle.component.scss']
})
export class PagetitleComponent implements OnInit {

  @Input() breadcrumbItems: BreadCrumbItem[] |  Array<{}>;
  @Input() title: string;

  constructor() { }

  ngOnInit() {
  }

}
