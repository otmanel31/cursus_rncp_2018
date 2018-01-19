import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Utilisateur } from '../models/utilisateur';
import { Subject } from 'rxjs/Subject';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class AuthManagerService {

  private currentUser : Utilisateur;
  private utilisateurSubject :  Subject<[boolean, Utilisateur]>;
  
  public getCurrentUser() : Utilisateur {
    return this.currentUser;
  }
  public setCurrentUser(utilisateur : Utilisateur) : void {
    this.currentUser = utilisateur;
    // publication du nouvel utilisateur loggé
    this.utilisateurSubject.next([true, this.currentUser]);
  }

  public isRoleActive(roleName : string) : boolean {
    //console.log("check role")
    //console.log(this.currentUser);
    // si pas d'utilisateur loggé ou aucun role, -> on ne peu pas avoir le role
    if (this.currentUser == null || this.currentUser.roles == null)
      return false;
    if (this.currentUser.roles.findIndex(r => r.roleName == roleName) != -1) {
      // role présent!
      return true;
    }
    else
      return false;
  }


  public logOut() : void {
    this.currentUser = null;
    // publication du fait qu'il n'y a plus d'utilisateur loggé
    this.utilisateurSubject.next([false, null]);
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
    this.utilisateurSubject = new Subject<[boolean, Utilisateur]>();
  }

  public getUtilisateurAsObservable() : Observable<[boolean, Utilisateur]> {
    return this.utilisateurSubject.asObservable();
  }


}
