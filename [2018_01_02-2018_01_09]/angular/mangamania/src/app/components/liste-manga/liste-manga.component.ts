import { Component, OnInit, OnDestroy } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { Manga } from '../../metier/manga';
import { MangaRepositoryService } from '../../services/manga-repository.service';
import { Subject } from 'rxjs/Subject';
import { Subscription } from 'rxjs/Subscription';

@Component({
  selector: 'app-liste-manga',
  templateUrl: './liste-manga.component.html',
  styleUrls: ['./liste-manga.component.css']
})
export class ListeMangaComponent implements OnInit, OnDestroy {

  ngOnDestroy(): void {
    this.mangasSubscription.unsubscribe();
  }

  public mangas : Subject<Manga[]>;
  public mangasSubscription : Subscription;
  public totalItems : number;
  public currentPage : number;
  constructor(private _mangaRepository: MangaRepositoryService) { }

  ngOnInit() {
    this.mangas = new Subject();
    this.mangasSubscription = this._mangaRepository.listeMangasObservable()
                                                   .subscribe(page => {
                                                     this.totalItems = page.totalElements;
                                                     this.currentPage = page.number + 1;
                                                     this.mangas.next(page.content);
                                                   });
     // je demande au service de rafraichir la liste a partir du backend rest
     this._mangaRepository.refreshListe();
  }

  public deleteManga(id: number) : void {
    this._mangaRepository.deleteManga(id)
                         .then( m => {
                            console.log("manga supprime: " + m.id);
                            this._mangaRepository.refreshListe();
                         });
  }

  public pageChanged(event : any) : void {
    this._mangaRepository.setNoPage(event.page - 1);
  }
}
