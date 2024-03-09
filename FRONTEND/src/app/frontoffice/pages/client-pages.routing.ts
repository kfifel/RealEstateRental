import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {PropertyDetailsComponent} from "./property-details/property-details.component";
import {PropertyResolver} from "../../backoffice/property/service/property.resolver";
import {PropertyListComponent} from "./property-list/property-list.component";

const routes: Routes = [
  {
    path: 'property/:id',
    component: PropertyDetailsComponent,
    resolve: {property: PropertyResolver}
  },
  {
    path: 'property',
    component: PropertyListComponent,
  }
]
@NgModule({
  imports: [
    RouterModule.forChild(routes)
  ]
})
export class ClientPagesRouting {

}
