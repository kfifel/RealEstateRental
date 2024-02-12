import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {RouterModule, Routes} from "@angular/router";
import {ClientDashboardComponent} from "./dashboard/Client-dashboard.component";

const routes: Routes = [
  {
    path: 'dashboard',
    component: ClientDashboardComponent
  },
  {
    path: '',
    redirectTo: 'dashboard',
    pathMatch: 'full'
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
