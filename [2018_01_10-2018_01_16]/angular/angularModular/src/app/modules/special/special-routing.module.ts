import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ListeSpecialeComponent } from './components/liste-speciale/liste-speciale.component';
import { DetailsSpecialeComponent } from './components/details-speciale/details-speciale.component';

const routes: Routes = [
  {path: 'liste', component: ListeSpecialeComponent},
  {path: 'details', component: DetailsSpecialeComponent}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class SpecialRoutingModule { }
