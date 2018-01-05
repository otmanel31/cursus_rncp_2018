import { Injectable } from '@angular/core';
import { Manga } from '../metier/manga';
import { Observable } from 'rxjs/Observable';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Page } from '../metier/page';

@Injectable()
export class MangaRepositoryService {

  private mangasSubject : BehaviorSubject<Page<Manga>>;
  private searchTitre : string; // recherche sur le titre

  private noPage : number;

  public setNoPage(no : number) : void {
    this.noPage = no;
    this.refreshListe();
  }

  constructor( private _http: HttpClient) {
      this.searchTitre = "";
      this.mangasSubject = new BehaviorSubject(new Page([], 0, 0, 5, 0, 1, true, false, null));
      this.noPage = 0;
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
      let url = "http://localhost:8080/mangamaniaForm/pmangas";
      if (this.searchTitre != "") {
        url += `/search/${this.searchTitre}`;
      }
      url += `?page=${this.noPage}`;
      this._http.get<Page<Manga>>(url)
                .toPromise()
                .then(mangas => this.mangasSubject.next(mangas))
   }


   public findManga(id : number) : Promise<Manga> {
      let url = `http://localhost:8080/mangamaniaForm/mangas/${id}`;
      return this._http.get<Manga>(url).toPromise();
   }


   public saveManga(manga: Manga) : Promise<Manga> {
     const httpOptions = {
       headers: new HttpHeaders({'Content-Type' : 'application/json'})
     };
     if (manga.id == 0) {
       // insertion
       return this._http.post<Manga>("http://localhost:8080/mangamaniaForm/mangas",
                                      manga,
                                      httpOptions)
                                      .toPromise();
     }
     else {
       // update
       return this._http.put<Manga>("http://localhost:8080/mangamaniaForm/mangas",
                                      manga,
                                      httpOptions)
                                      .toPromise();
     }
   }

   public deleteManga(id : number) : Promise<Manga> {
    let url = `http://localhost:8080/mangamaniaForm/mangas/${id}`;
    return this._http.delete<Manga>(url).toPromise();
 }

}
