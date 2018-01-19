import { Injectable } from '@angular/core';
import { Subject } from 'rxjs/Subject';
import { Observable } from 'rxjs/Observable';
import { HttpErrorResponse } from '@angular/common/http';

@Injectable()
export class AlertManagerService {


  private alertsSubject : Subject<[string, string]>;

  constructor() { 
    this.alertsSubject = new Subject();
  }

  public alertsAsObservable() : Observable<[string, string]> {
    return this.alertsSubject.asObservable();
  }

  public handleMessage(type : string, message: string) : void {
    this.alertsSubject.next([type,message]);
  }

  public handleErrorResponse(error : HttpErrorResponse) : void {
    //console.log("erreur recue :" + error.status);
    if (error.status == 401) return;
    if (error.status == 403) {
      this.alertsSubject.next(["danger", "you can't access this"]);
      return;
    }
    if (error.status == 404) {
      this.alertsSubject.next(["warning", "server dosn't seem online, try later again"]);
      return;
    }
    if (error.status == 500) {
      this.alertsSubject.next(["warning", "server caught fire, help under way: " + error.statusText]);
      return;
    }
    else {
      this.alertsSubject.next(["warning", "autre erreur"]);
    }
    
  }
  

}
