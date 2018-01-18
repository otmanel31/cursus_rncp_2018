import { Component, OnInit, OnDestroy } from '@angular/core';
import { AuthManagerService } from '../../services/auth-manager.service';
import { Utilisateur } from '../../models/utilisateur';
import { Observable } from 'rxjs/Observable';
import { Subscription } from 'rxjs/Subscription';
import { Router } from '@angular/router';

@Component({
  selector: 'app-utilisateur-info',
  templateUrl: './utilisateur-info.component.html',
  styleUrls: ['./utilisateur-info.component.css']
})
export class UtilisateurInfoComponent implements OnInit, OnDestroy {


  public isLoggedIn : boolean;
  public currentUser : Utilisateur;
  private currentUserSubscription : Subscription;

  constructor(private authManager : AuthManagerService, private router : Router) { }

  ngOnInit() {
    this.isLoggedIn = this.authManager.isLoggedIn();
    this.currentUser = this.authManager.getCurrentUser();
    this.currentUserSubscription = 
          this.authManager.getUtilisateurAsObservable()
                          .subscribe( u => {
                                            this.isLoggedIn = u[0];
                                            this.currentUser = u[1];
                                          });
  }

  logmeout() : boolean {
    this.authManager.logOut();
    this.router.navigateByUrl("/login");
    return false;
  }

  ngOnDestroy(): void {
    this.currentUserSubscription.unsubscribe();
  }

}
