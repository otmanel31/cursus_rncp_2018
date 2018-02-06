import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'app';

  public message = "coucou";

  public compteur : number = 1;

  public incrementeCompteur() : void {
    this.compteur = this.compteur + 1;
  }
}
