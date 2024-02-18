import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { NgbCollapseModule, NgbDatepickerModule, NgbTimepickerModule, NgbDropdownModule } from '@ng-bootstrap/ng-bootstrap';

import { PagetitleComponent } from './pagetitle/pagetitle.component';
import {RouterModule} from "@angular/router";

@NgModule({
  declarations: [PagetitleComponent],
    imports: [
        CommonModule,
        FormsModule,
        NgbCollapseModule,
        NgbDatepickerModule,
        NgbTimepickerModule,
        NgbDropdownModule,
        RouterModule
    ],
  exports: [PagetitleComponent]
})
export class UIModule { }
