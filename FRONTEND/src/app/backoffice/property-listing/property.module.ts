import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PropertyListComponent } from './property-list/property-list.component';
import {RouterModule, Routes} from "@angular/router";
import {SharedModule} from "../../shared/shared.module";
import {PropertyResolver} from "./service/property.resolver";


let routes: Routes = [
  { path: '', component: PropertyListComponent },
  {
    path: ':id',
    component: PropertyListComponent,
    resolve: { property: PropertyResolver}
  },
];

@NgModule({
  declarations: [
    PropertyListComponent
  ],
  imports: [
    CommonModule,
    RouterModule.forChild(routes),
    SharedModule
  ]
})
export class PropertyModule { }
