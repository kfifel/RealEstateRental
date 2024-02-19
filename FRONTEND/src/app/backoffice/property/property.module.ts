import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PropertyListComponent } from './property-list/property-list.component';
import {RouterModule, Routes} from "@angular/router";
import {SharedModule} from "../../shared/shared.module";
import {PropertyResolver} from "./service/property.resolver";
import {UIModule} from "../../shared/ui/ui.module";
import {NgbDropdownModule, NgbNavModule, NgbPaginationModule} from "@ng-bootstrap/ng-bootstrap";
import {PropertyCreateComponent} from "./property-create/property-create.component";
import {DropzoneModule} from "ngx-dropzone-wrapper";
import {NgxDropzoneModule} from "ngx-dropzone";
import {CKEditorModule} from "@ckeditor/ckeditor5-angular";
import {FormsModule} from "@angular/forms";
import {UiSwitchModule} from "ngx-ui-switch";
import { PropertyDetailComponent } from './property-detail/property-detail.component';


let routes: Routes = [
  { path: '', component: PropertyListComponent },
  { path: 'new', component: PropertyCreateComponent },
  {
    path: ':id',
    component: PropertyDetailComponent,
    resolve: { property: PropertyResolver}
  },
];

@NgModule({
  declarations: [
    PropertyListComponent,
    PropertyCreateComponent,
    PropertyDetailComponent
  ],
    imports: [
        CommonModule,
        RouterModule.forChild(routes),
        SharedModule,
        UIModule,
        NgbDropdownModule,
        DropzoneModule,
        NgxDropzoneModule,
        CKEditorModule,
        FormsModule,
        UiSwitchModule,
        NgbNavModule,
        NgbPaginationModule
    ]
})
export class PropertyModule { }
