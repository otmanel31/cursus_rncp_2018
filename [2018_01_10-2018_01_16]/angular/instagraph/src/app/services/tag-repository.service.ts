import { Injectable } from '@angular/core';

import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';

import { Tag } from "../models/tag";
import { Page } from "../models/page";

import { Observable } from 'rxjs/Observable';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { Image } from '../models/image';




@Injectable()
export class TagRepositoryService {
  private tagsSubject : BehaviorSubject<Page<Tag>>;
  private noPage : number;
  private taillePage : number;
  private searchTerm : string = "";

  private baseUrlApi: string = "http://localhost:8080/api/tags";
  private baseUrlExtendedApi : string = "http://localhost:8080/extendedapi/tag";


   constructor(private _http: HttpClient) {
    this.noPage = 0;
    this.taillePage = 15;
    this.tagsSubject = new BehaviorSubject(Page.emptyPage<Tag>());
   }

   public setSearchTerm(searchTerm: string) : void {
     this.searchTerm = searchTerm;
    this.refreshListe();
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
    // gestion de la recherche de tag
    if (this.searchTerm != null && this.searchTerm.length > 0) {
      urlparams = urlparams.set("search", "" + this.searchTerm);
    }

    this._http.get<Page<Tag>>(`${this.baseUrlExtendedApi}/liste`,
                                 {params: urlparams})
              .toPromise()
              .then( p => this.tagsSubject.next(p))
              .catch(e => console.log("pas de tag recu"));
   }

   public addTags(tags : Tag[], images: Image[]) : Promise<Tag[]> {
     let contentIds : string = images.map(img => img.id).join(",");
     let tagIds : string = tags.map(tag => tag.id).join(",");
     let url = `${this.baseUrlExtendedApi}/add/${contentIds}/${tagIds}`;
     return this._http.post<Tag[]>(url, {})
                      .toPromise();
   }

   public removeTags(tags : Tag[], images: Image[]) : Promise<Tag[]> {
    let contentIds : string = images.map(img => img.id).join(",");
    let tagIds : string = tags.map(tag => tag.id).join(",");
    let url = `${this.baseUrlExtendedApi}/remove/${contentIds}/${tagIds}`;
    return this._http.post<Tag[]>(url, {})
                     .toPromise();
  }

  public createTag( tag : Tag) : Promise<Tag> {
    let url = this.baseUrlApi;
    return this._http.post<Tag>(url, tag)
                     .toPromise();
  }

   // recuperer la liste des tags associé à un contenu précis
   public getRelatedTags(contentid: number) : Promise<Tag[]> {
     let url : string  = `${this.baseUrlExtendedApi}/findrelatedto/${contentid}`;
      return this._http.get<Tag[]>(url)
                  .toPromise(); 
   }

}
