import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import {
    NgbNavModule,
    NgbDropdownModule,
    NgbModalModule,
    NgbTooltipModule,
    NgbCollapseModule,
    NgbPaginationModule
} from '@ng-bootstrap/ng-bootstrap';
import { NgApexchartsModule } from 'ng-apexcharts';
import { FullCalendarModule } from '@fullcalendar/angular';
import { SimplebarAngularModule } from 'simplebar-angular';
import dayGridPlugin from '@fullcalendar/daygrid'; // a plugin
import interactionPlugin from '@fullcalendar/interaction'; // a plugin
import bootstrapPlugin from "@fullcalendar/bootstrap";
import { LightboxModule } from 'ngx-lightbox';

import { WidgetModule } from '../shared/widget/widget.module';
import { UIModule } from '../shared/ui/ui.module';

import { BackofficeRoutingModule } from './backoffice-routing.module';

import { DashboardsModule } from './dashboards/dashboards.module';
import { HttpClientModule } from '@angular/common/http';
import { CompetitionComponent } from './competition/competition.component';
import { UserManagementComponent } from './user-management/user-management.component';
import {UiSwitchModule} from "ngx-ui-switch";
import {NgSelectModule} from "@ng-select/ng-select";

FullCalendarModule.registerPlugins([ // register FullCalendar plugins
  dayGridPlugin,
  interactionPlugin,
  bootstrapPlugin
]);

@NgModule({
  declarations: [
    CompetitionComponent,
    UserManagementComponent
  ],
    imports: [
        CommonModule,
        FormsModule,
        NgbDropdownModule,
        NgbModalModule,
        BackofficeRoutingModule,
        NgApexchartsModule,
        ReactiveFormsModule,
        DashboardsModule,
        HttpClientModule,
        UIModule,
        WidgetModule,
        FullCalendarModule,
        NgbNavModule,
        NgbTooltipModule,
        NgbCollapseModule,
        SimplebarAngularModule,
        LightboxModule,
        NgbPaginationModule,
        UiSwitchModule,
        NgSelectModule
    ],
})
export class BackofficeModule { }
