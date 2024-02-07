import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DashboaardComponent } from './dashboaard/dashboaard.component';
import {AdminRoutingModule} from "./admin.routing";


@NgModule({
  declarations: [
    DashboaardComponent,
  ],
  imports: [
    CommonModule,
    AdminRoutingModule
  ]
})
export class AdminModule { }
