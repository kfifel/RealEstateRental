import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {PropertyDetailsComponent} from "./property-details/property-details.component";
import {PropertyResolver} from "../../backoffice/property/service/property.resolver";
import {PropertyListComponent} from "./property-list/property-list.component";
import {AuthGuard} from "../../core/guards/auth.guard";

const routes: Routes = [
  {
    path: 'property/:id',
    component: PropertyDetailsComponent,
    resolve: {property: PropertyResolver}
  },
  {
    path: 'property',
    canActivate: [AuthGuard],
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
