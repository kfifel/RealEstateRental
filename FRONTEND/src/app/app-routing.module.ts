import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { AuthGuard } from './core/guards/auth.guard';
import { LayoutComponent } from './layouts/layout.component';
import { Page404Component } from './extrapages/page404/page404.component';
import {BackOfficeGuard} from "./core/guards/back-office-guard.service";

const routes: Routes = [
  { path: 'account', loadChildren: () => import('./account/account.module').then(m => m.AccountModule) },
  // tslint:disable-next-line: max-line-length
  { path: 'admin',
    component: LayoutComponent,
    loadChildren: () => import('./backoffice/backoffice.module').then(m => m.BackofficeModule),
    canActivate: [BackOfficeGuard, AuthGuard]
  },
  {
    path: '',
    loadChildren: () => import('./frontoffice/client-front-office.module').then(m => m.ClientFrontOfficeModule),
  },
  { path: '**', component: Page404Component },
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { scrollPositionRestoration: 'top', relativeLinkResolution: 'legacy' })],
  exports: [RouterModule]
})

export class AppRoutingModule { }
