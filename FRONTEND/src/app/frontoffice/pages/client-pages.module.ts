import {NgModule} from "@angular/core";
import {ClientPagesRouting} from './client-pages.routing'
import {PropertyDetailsComponent} from "./property-details/property-details.component";
import {CommonModule} from "@angular/common";
import {SharedModule} from "../../shared/shared.module";
import {UIModule} from "../../shared/ui/ui.module";
import {NgbDatepickerModule, NgbNavModule} from "@ng-bootstrap/ng-bootstrap";
import {FormsModule} from "@angular/forms";
import { PropertyListComponent } from './property-list/property-list.component';
import {RouterModule} from "@angular/router";

@NgModule({
  declarations: [
    PropertyDetailsComponent,
    PropertyListComponent
  ],
  imports: [
    CommonModule,
    ClientPagesRouting,
    SharedModule,
    UIModule,
    NgbNavModule,
    NgbDatepickerModule,
    FormsModule,
    RouterModule,
  ],
})
export class ClientPagesModule2 {

}
