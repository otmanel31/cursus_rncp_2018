import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';

import { Image } from "../models/image";
import { Page } from "../models/page";

import { Observable } from 'rxjs/Observable';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { Tag } from '../models/tag';
import { Subject } from 'rxjs/Subject';


@Injectable()
export class ImageRepositoryService {

  // la liste des images renvoyée par le serveur sera publiée ici
  private imagesSubject : BehaviorSubject<Page<Image>>;
  private noPage : number;
  private taillePage : number;
  // tag selectionnés
  private selectedTags : Tag[];
  private selectedTagsSubject : BehaviorSubject<Tag[]>;

  private baseUrlApi: string = "http://localhost:8080/api/images";
  private baseUrlExtendedApi : string = "http://localhost:8080/extendedapi/image";

  constructor(private _http: HttpClient) {
    // initialisation images
    this.noPage = 0;
    this.taillePage = 12;
    this.imagesSubject = new BehaviorSubject(Page.emptyPage<Image>());
    // initialisation tags
    this.selectedTags = [];
    this.selectedTagsSubject = new BehaviorSubject(this.selectedTags);
   }

   public addSelectedTag(tag : Tag) {
     if (this.selectedTags.findIndex(t => t.id == tag.id) == -1) {
       // tag non présent, on peut l'ajouter
       this.selectedTags.push(tag);
       // je préviens ceux ecoutant la liste des tags selectionnés
       this.selectedTagsSubject.next(this.selectedTags);
       // et je rafraichi la liste des images
       this.refreshListe();
     }
   }

   public removeSelectedTag(tag : Tag) {
      let index = this.selectedTags.findIndex(t => t.id == tag.id);
      if (index != -1) {
        // retrait du tag du tableau (position, nombre)
        this.selectedTags.splice(index, 1);
        this.selectedTagsSubject.next(this.selectedTags);
        this.refreshListe();
      }
   }


   public setNoPage(noPage : number) : void {
     this.noPage = noPage;
     this.refreshListe();
   }

   public selectedtagsAsObservable() : Observable<Tag[]> {
     return this.selectedTagsSubject.asObservable();
   }

   public listeImageAsObservable() : Observable<Page<Image>> {
     return this.imagesSubject.asObservable();
   }

   public refreshListe() : void {
    let urlparams : HttpParams = new HttpParams();
    // gestion de la pagination
    urlparams = urlparams.set("page", "" + this.noPage);
    // gestion du filtrage par tag
    if (this.selectedTags.length > 0) {
      // liste de ids de tags séparé par des virgules dans le parametre
      urlparams = urlparams.set("tagsId",
          this.selectedTags.map(t => "" + t.id).join(",")
        );
    }

    this._http.get<Page<Image>>(`${this.baseUrlExtendedApi}/findbytagfull`,
                                 {params: urlparams})
              .toPromise()
              .then( p => this.imagesSubject.next(p));
   }

   public deleteImages(ids : number[]) : void {
     // [4,6,8] -> "4,6,8"
      let ids_string = ids.join(",");
      let urlparams : HttpParams = new HttpParams();
      urlparams = urlparams.set("imagesId", ids_string);
      // appeler delete sur le serveur avec les ids
      this._http.delete<Map<string, number>>(`${this.baseUrlExtendedApi}/delete`,
                                            {params : urlparams})
                .toPromise()
                .then( result => {
                  console.log(result);
                  this.refreshListe();
                });
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
