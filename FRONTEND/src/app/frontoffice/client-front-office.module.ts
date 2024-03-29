import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {ClientDashboardComponent} from "./dashboard/Client-dashboard.component";
import {ClientDashboardRoutingModule} from "./client-front-office.routing";
import {NgbAccordionModule, NgbDatepickerModule, NgbNavModule, NgbTooltipModule} from "@ng-bootstrap/ng-bootstrap";
import {ScrollToModule} from "@nicky-lenaers/ngx-scroll-to";
import {LayoutsModule} from "../layouts/layouts.module";
import {ExtrapagesModule} from "../extrapages/extrapages.module";
import {CarouselModule} from "ngx-owl-carousel-o";
import * as ClientOfficeShardMadule from "./shared/shared.module";
import {SharedModule}  from "../shared/shared.module";
import {RouterModule} from "@angular/router";
import {PropertyModule} from "../backoffice/property/property.module";
import {UIModule} from "../shared/ui/ui.module";
import {ArchwizardModule} from "angular-archwizard";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {NgSelectModule} from "@ng-select/ng-select";


@NgModule({
  declarations: [ClientDashboardComponent],
  imports: [
    CommonModule,
    ClientDashboardRoutingModule,
    NgbAccordionModule,
    NgbNavModule,
    ScrollToModule,
    LayoutsModule,
    ExtrapagesModule,
    CarouselModule,
    NgbAccordionModule,
    NgbNavModule,
    NgbTooltipModule,
    SharedModule,
    ClientOfficeShardMadule.SharedModule,
    RouterModule,
    PropertyModule,
    UIModule,
    FormsModule,
    ReactiveFormsModule,
    NgSelectModule,
    NgbDatepickerModule
  ]
})
export class ClientFrontOfficeModule { }
