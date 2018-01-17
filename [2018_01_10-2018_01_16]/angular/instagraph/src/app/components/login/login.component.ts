import { Component, OnInit } from '@angular/core';
import { AuthManagerService } from '../../services/auth-manager.service';
import { Subscription } from 'rxjs/Subscription';
import { OnDestroy } from '@angular/core/src/metadata/lifecycle_hooks';
import { HttpClient } from '@angular/common/http';
import { Utilisateur } from '../../models/utilisateur';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit, OnDestroy {
  public userlogin : any;
  private baseUrlApi: string = "http://localhost:8080/extendedapi/auth";

  constructor(private authManager : AuthManagerService,
             private _http : HttpClient,
             private router : Router) {    
  }

  ngOnInit() {
    this.userlogin = {};
  }
  ngOnDestroy(): void {}

  public trylogin() : void {
    console.log("tentative de login avec " + this.userlogin.username);

    let newuser = new Utilisateur(this.userlogin.username,
                                  this .userlogin.password, true);
    this.authManager.setCurrentUser(newuser);
    this._http.post<Utilisateur>(`${this.baseUrlApi}/login`, newuser)
              .subscribe( u => {
                // l'utilisateur nouvellement loggé devient le currentuser
                //this.authManager.setCurrentUser(u);
                console.log("je suis bien loggé avec " + u.username);
                this.router.navigateByUrl("/liste");
            });
    
    
  }
}
