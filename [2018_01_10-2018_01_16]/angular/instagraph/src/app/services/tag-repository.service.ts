import { Injectable } from '@angular/core';

import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';

import { Tag } from "../models/tag";
import { Page } from "../models/page";

import { Observable } from 'rxjs/Observable';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';


@Injectable()
export class TagRepositoryService {
  private tagsSubject : BehaviorSubject<Page<Tag>>;
  private noPage : number;
  private taillePage : number;

  private baseUrlApi: string = "http://localhost:8080/api/tags";
  private baseUrlExtendedApi : string = "http://localhost:8080/extendedapi/tag";


   constructor(private _http: HttpClient) {
    this.noPage = 0;
    this.taillePage = 15;
    this.tagsSubject = new BehaviorSubject(Page.emptyPage<Tag>());
   }

   public setNoPage(noPage : number) : void {
    this.noPage = noPage;
    this.refreshListe();
  }

  public listetagAsObservable() : Observable<Page<Tag>> {
    return this.tagsSubject.asObservable();
  }

   public refreshListe() : void {
    let urlparams : HttpParams = new HttpParams();
    urlparams = urlparams.set("page", "" + this.noPage);

    this._http.get<Page<Tag>>(`${this.baseUrlExtendedApi}/liste`,
                                 {params: urlparams})
              .toPromise()
              .then( p => this.tagsSubject.next(p))
              .catch(e => console.log("pas de tag recu"));
   }

}
