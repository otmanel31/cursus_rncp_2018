import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';

import { Image } from "../models/image";
import { Page } from "../models/page";

import { Observable } from 'rxjs/Observable';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';

@Injectable()
export class ImageRepositoryService {

  // la liste des images renvoyée par le serveur sera publiée ici
  private imagesSubject : BehaviorSubject<Page<Image>>;
  private noPage : number;
  private taillePage : number;

  private baseUrlApi: string = "http://localhost:8080/api/images";
  private baseUrlExtendedApi : string = "http://localhost:8080/extendedapi/image";

  constructor(private _http: HttpClient) {
    this.noPage = 0;
    this.taillePage = 12;
    this.imagesSubject = new BehaviorSubject(Page.emptyPage<Image>());
   }

   public setNoPage(noPage : number) : void {
     this.noPage = noPage;
     this.refreshListe();
   }

   public listeImageAsObservable() : Observable<Page<Image>> {
     return this.imagesSubject.asObservable();
   }

   public refreshListe() : void {
    let urlparams : HttpParams = new HttpParams();
    urlparams = urlparams.set("page", "" + this.noPage);

    this._http.get<Page<Image>>(`${this.baseUrlExtendedApi}/findbytag`,
                                 {params: urlparams})
              .toPromise()
              .then( p => this.imagesSubject.next(p));
   }

   public getImageThumbUrl(id: number) : string {
     return `${this.baseUrlExtendedApi}/downloadthumb/${id}`;
   }
   public getImageUrl(id: number) : string {
    return `${this.baseUrlExtendedApi}/download/${id}`;
  }

  public getUploadurl() : string {
    return  `${this.baseUrlExtendedApi}/upload`;
  }
  

}
