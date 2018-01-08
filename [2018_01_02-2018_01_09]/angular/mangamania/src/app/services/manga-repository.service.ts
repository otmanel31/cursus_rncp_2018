import { Injectable } from '@angular/core';
import { Manga } from '../metier/manga';
import { Observable } from 'rxjs/Observable';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { Page } from '../metier/page';

@Injectable()
export class MangaRepositoryService {

  private mangasSubject : BehaviorSubject<Page<Manga>>;
  private searchTitre : string; // recherche sur le titre
  private filterByRatingMin : number;

  private noPage : number;
  private baseUrl: string = "http://localhost:8080/mangamaniaForm/";

  public setNoPage(no : number) : void {
    this.noPage = no;
    this.refreshListe();
  }

  public setFilterByRatingMin(rating : number) : void {
    this.filterByRatingMin = rating;
    this.refreshListe();
  }

  constructor( private _http: HttpClient) {
      this.searchTitre = "";
      this.mangasSubject = new BehaviorSubject(new Page([], 0, 0, 5, 0, 1, true, false, null));
      this.noPage = 0;
      this.filterByRatingMin = 0;
   }

   // methode appelée si un composant veut modifier le filtrage de la liste des mangas
   // en fonction du titre
   public changeSearch(searchTerm : string) : void {
     this.searchTitre = searchTerm;
     // rafraichir la liste
     this.refreshListe();
   }

   public listeMangasObservable() : Observable<Page<Manga>> {
      return this.mangasSubject.asObservable();
   }

   public refreshListe() : void {
     // quand on veut rafraichir la liste (pour une raison ou une autre)
     // on envoie une requette au serveur
     // quand on recoit sa reponse, on republie les donnée
     // dans le sujet "mangasSubjet"
     // ainsi, tous ceux qui ecoute le sujet "mangasSubject"
     // recevront la liste rafraichie
      let url = `${this.baseUrl}pmangas`;
      if (this.searchTitre != "") {
        url += `/search/${this.searchTitre}`;
      }
      // attention, URLParams est immutable !
      // cet objet permet de déléguer a angular la construction de l'url après ?
      // autrement dit la gestion des query parameters
      let urlparams : HttpParams = new HttpParams();
      urlparams = urlparams.set("page", "" + this.noPage);
      if (this.filterByRatingMin > 0) {
        urlparams = urlparams.set("ratingMinimum", "" + this.filterByRatingMin);
      }
      this._http.get<Page<Manga>>(url, { "params" : urlparams})
                .toPromise()
                .then(mangas => this.mangasSubject.next(mangas))
   }


   public findManga(id : number) : Promise<Manga> {
      let url = `${this.baseUrl}mangas/${id}`;
      return this._http.get<Manga>(url).toPromise();
   }


   public saveManga(manga: Manga) : Promise<Manga> {
     const httpOptions = {
       headers: new HttpHeaders({'Content-Type' : 'application/json'})
     };
     if (manga.id == 0) {
       // insertion
       return this._http.post<Manga>(`${this.baseUrl}mangas`,
                                      manga,
                                      httpOptions)
                                      .toPromise();
     }
     else {
       // update
       return this._http.put<Manga>(`${this.baseUrl}mangas`,
                                      manga,
                                      httpOptions)
                                      .toPromise();
     }
   }

   public deleteManga(id : number) : Promise<Manga> {
    let url = `${this.baseUrl}mangas/${id}`;
    return this._http.delete<Manga>(url).toPromise();
 }

}
