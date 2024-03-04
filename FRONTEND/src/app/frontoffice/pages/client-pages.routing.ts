import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {PropertyDetailsComponent} from "./property-details/property-details.component";
import {PropertyResolver} from "../../backoffice/property/service/property.resolver";

const routes: Routes = [
  {
    path: 'property/:id',
    component: PropertyDetailsComponent,
    resolve: {property: PropertyResolver}
  }
]
@NgModule({
  imports: [
    RouterModule.forChild(routes)
  ]
})
export class ClientPagesRouting {

}
