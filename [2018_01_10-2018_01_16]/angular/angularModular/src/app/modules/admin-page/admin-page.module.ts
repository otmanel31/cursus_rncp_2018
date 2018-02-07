import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UtilisateurListeComponent } from './components/utilisateur-liste/utilisateur-liste.component';

@NgModule({
  imports: [
    CommonModule
  ],
  declarations: [UtilisateurListeComponent],
  /*exports permet d'indiquer ce qui doit
   être accessible de l'exterieur du module */
  exports: [
    UtilisateurListeComponent
  ]
})
export class AdminPageModule { }
