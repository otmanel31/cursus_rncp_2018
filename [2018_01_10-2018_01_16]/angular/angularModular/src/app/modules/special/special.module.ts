import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { SpecialRoutingModule } from './special-routing.module';
import { ListeSpecialeComponent } from './components/liste-speciale/liste-speciale.component';
import { DetailsSpecialeComponent } from './components/details-speciale/details-speciale.component';

@NgModule({
  imports: [
    CommonModule,
    SpecialRoutingModule
  ],
  declarations: [ListeSpecialeComponent, DetailsSpecialeComponent]
})
export class SpecialModule { }
