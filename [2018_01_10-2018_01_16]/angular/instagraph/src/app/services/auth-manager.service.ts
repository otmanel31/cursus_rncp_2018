import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Utilisateur } from '../models/utilisateur';
import { Subject } from 'rxjs/Subject';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class AuthManagerService {

  private currentUser : Utilisateur;
  private utilisateurSubject :  Subject<Utilisateur>;
  
  public getCurrentUser() : Utilisateur {
    return this.currentUser;
  }
  public setCurrentUser(utilisateur : Utilisateur) : void {
    this.currentUser = utilisateur;
  }

  public isLoggedIn() : boolean {
    if (this.currentUser == null)
      return false;
    else
      return true;
  }

  public getCredentials() : string {
    // generation de la valeur des credentials pour authentification
    return window.btoa(this.currentUser.username + ":" + this.currentUser.password);
  }
 
  constructor() {
    this.currentUser = null;
    this.utilisateurSubject = new Subject<Utilisateur>();
  }

  public getUtilisateurAsObservable() : Observable<Utilisateur> {
    return this.utilisateurSubject.asObservable();
  }


}
