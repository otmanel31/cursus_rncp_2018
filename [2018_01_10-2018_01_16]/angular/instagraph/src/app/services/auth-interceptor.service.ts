import { Injectable } from '@angular/core';
import { HttpInterceptor,
         HttpRequest,
         HttpHandler,
         HttpSentEvent,
         HttpHeaderResponse,
         HttpProgressEvent,
         HttpResponse,
         HttpUserEvent } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { AuthManagerService } from './auth-manager.service';

import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/do';
import 'rxjs/add/observable/throw';
import 'rxjs/add/observable/empty';

import { HttpErrorResponse } from '@angular/common/http';
import { Router } from '@angular/router';


@Injectable()
export class AuthInterceptorService implements HttpInterceptor
{

  constructor(private authManager : AuthManagerService, private router : Router) { }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpSentEvent 
                                                                | HttpHeaderResponse 
                                                                | HttpProgressEvent
                                                                | HttpResponse<any>
                                                                | HttpUserEvent<any>> {
    // AVANT envoie de la requette au serveur
    // console.log("requette interceptée: " + req.url);
    if (this.authManager.isLoggedIn()) {
      req = req.clone({ setHeaders: {
            Authorization: `Basic ${this.authManager.getCredentials()}`
          }});
    }
    // envoie de la requette a la suite
    return next.handle(req)
               .catch((error, caught) => {
                 console.log("erreur de la reponse");
                 //console.log(error);
                 if (error instanceof HttpErrorResponse) {
                   let resp : HttpErrorResponse = error;
                   if (resp.status == 401) {
                     // besoin d'authentification -> aller a la page du login
                     this.router.navigateByUrl("/login");
                   }
                 }
                 // retranmission de l'erreur
                 return Observable.throw(error);
               });
    
    /*.pipe( evt => {
        console.log("traitement reponse");
        console.log(evt);
        return evt;
    });*/
  }

}
