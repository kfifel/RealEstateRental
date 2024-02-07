import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {DashboaardComponent} from "./dashboaard/dashboaard.component";


const routes: Routes = [
  { path: "", component: DashboaardComponent}
]

@NgModule({
  imports: [
    RouterModule.forChild(routes)
  ]
})
export class AdminRoutingModule {

}
