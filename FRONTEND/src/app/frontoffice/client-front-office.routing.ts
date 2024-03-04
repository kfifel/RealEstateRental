import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {RouterModule, Routes} from "@angular/router";
import {ClientDashboardComponent} from "./dashboard/Client-dashboard.component";
import {PropertyDetailsComponent} from "./pages/property-details/property-details.component";
import {PropertyResolver} from "../backoffice/property/service/property.resolver";
import {LayoutComponent} from "../layouts/layout.component";
import {HorizontalComponent} from "../layouts/horizontal/horizontal.component";

const routes: Routes = [
  {
    path: 'dashboard',
    component: ClientDashboardComponent
  },
  {
    path: '',
    redirectTo: 'dashboard',
    pathMatch: 'full'
  },
  {
    path: 'client',
    component: HorizontalComponent,
    loadChildren: () => import('./pages/client-pages.module').then(m => m.ClientPagesModule2)
  }
];

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    RouterModule.forChild(routes)
  ]
})
export class ClientDashboardRoutingModule {}
