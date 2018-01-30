import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';

import { Image } from "../models/image";
import { Page } from "../models/page";

import { Observable } from 'rxjs/Observable';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { Tag } from '../models/tag';
import { Subject } from 'rxjs/Subject';

import { AlertManagerService } from './alert-manager.service';


@Injectable()
export class ImageRepositoryService {

  // la liste des images renvoyée par le serveur sera publiée ici
  private imagesSubject : BehaviorSubject<Page<Image>>;
  private noPage : number;
  private taillePage : number;
  // tag selectionnés
  private selectedTags : [boolean, Tag][];
  private selectedTagsSubject : BehaviorSubject<[boolean, Tag][]>;

  private baseUrlApi: string = "http://localhost:8080/api/images";
  private baseUrlExtendedApi : string = "http://localhost:8080/extendedapi/image";

  constructor(private _http: HttpClient,
              private alertManager : AlertManagerService) {
    // initialisation images
    this.noPage = 0;
    this.taillePage = 12;
    this.imagesSubject = new BehaviorSubject(Page.emptyPage<Image>());
    // initialisation tags
    this.selectedTags = [];
    this.selectedTagsSubject = new BehaviorSubject(this.selectedTags);
   }

   public addSelectedTag(tag : Tag) {
     if (this.selectedTags.findIndex(t => t[1].id == tag.id) == -1) {
       // tag non présent, on peut l'ajouter
       this.selectedTags.push([true, tag]);
       // je préviens ceux ecoutant la liste des tags selectionnés
       this.selectedTagsSubject.next(this.selectedTags);
       // et je rafraichi la liste des images
       this.refreshListe();
     }
   }

   public removeSelectedTag(tag : Tag) {
      let index = this.selectedTags.findIndex(t => t[1].id == tag.id);
      if (index != -1) {
        // retrait du tag du tableau (position, nombre)
        this.selectedTags.splice(index, 1);
        this.selectedTagsSubject.next(this.selectedTags);
        this.refreshListe();
      }
   }

   // permet de modifier le flag inclus/exclus d'un tag déjà séléctionné
  public updateSelectedTag(tag : [boolean, Tag]) {
      let index = this.selectedTags.findIndex(t => t[1].id == tag[1].id);
      if (index != -1) {
        // retrait du tag du tableau (position, nombre)
        this.selectedTags[index][0] = tag[0];
        this.selectedTagsSubject.next(this.selectedTags);
        this.refreshListe();
      }
   }
   

   public setNoPage(noPage : number) : void {
     this.noPage = noPage;
     this.refreshListe();
   }

   public selectedtagsAsObservable() : Observable<[boolean,Tag][]> {
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
    // separation entre tag a inclure et tag a exclure
    let positiveTags =  this.selectedTags.filter(t => t[0]);
    let negativeTags =  this.selectedTags.filter(t => !t[0]);

    if (positiveTags.length > 0) {
      // liste de ids de tags séparé par des virgules dans le parametre
      urlparams = urlparams.set("tagsId",
      positiveTags.map(t => "" + t[1].id).join(",")
        );
    }
    if (negativeTags.length > 0) {
      // liste de ids de tags séparé par des virgules dans le parametre
      urlparams = urlparams.set("negativetagsId",
      negativeTags.map(t => "" + t[1].id).join(",")
        );
    }
    

    this._http.get<Page<Image>>(`${this.baseUrlExtendedApi}/findbytagfull`,
                                 {params: urlparams})
              .toPromise()
              .then( p => this.imagesSubject.next(p))
              .catch( e =>  {
                console.log("erreur a retransmettre");
                this.alertManager.handleErrorResponse(e);
              });
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
                  this.alertManager.handleMessage("success", "image successfully deleted");
                  this.refreshListe();
                }, err  => {
                  this.alertManager.handleErrorResponse(err);
                });
   }

   public findImage(id: number) : Promise<Image> {
     let url = `${this.baseUrlExtendedApi}/findone/${id}`;
     return this._http.get<Image>(url).toPromise();
   }

   public updateImage(image : Image) : Promise<Image> {
    let urlparams : HttpParams = new HttpParams();
    urlparams = urlparams.set("name", image.name)
                         .set("description", image.description)
                         .set("fileName", image.fileName);
    let url = `${this.baseUrlExtendedApi}/saveone/${image.id}`;
    return this._http.put<Image>(url, {}, {params: urlparams})
               .toPromise();
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
