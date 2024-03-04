import {NgModule} from "@angular/core";
import {ClientPagesRouting} from './client-pages.routing'
import {PropertyDetailsComponent} from "./property-details/property-details.component";
import {CommonModule} from "@angular/common";
import {SharedModule} from "../../shared/shared.module";
import {UIModule} from "../../shared/ui/ui.module";
import {NgbNavModule} from "@ng-bootstrap/ng-bootstrap";

@NgModule({
  declarations: [
    PropertyDetailsComponent
  ],
  imports: [
    CommonModule,
    ClientPagesRouting,
    SharedModule,
    UIModule,
    NgbNavModule,
  ],
})
export class ClientPagesModule2 {

}
