import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { DefaultComponent } from './dashboards/default/default.component';
import {UserManagementComponent} from "./user-management/user-management.component";
import {PropertyGuard} from "../core/guards/property.guard";

const routes: Routes = [
  { path: '', redirectTo: 'dashboard' },
  { path: 'dashboard', component: DefaultComponent },
  { path: 'users-management', component: UserManagementComponent },
  {
    path: 'property',
    canActivate: [PropertyGuard],
    loadChildren: () => import('./property/property.module').then(m => m.PropertyModule)
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class BackofficeRoutingModule { }
