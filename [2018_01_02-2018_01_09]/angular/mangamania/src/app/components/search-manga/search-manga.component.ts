import { Component, OnInit } from '@angular/core';
import { Subject } from 'rxjs/Subject';

import "rxjs/add/operator/debounceTime";
import { MangaRepositoryService } from '../../services/manga-repository.service';

@Component({
  selector: 'app-search-manga',
  templateUrl: './search-manga.component.html',
  styleUrls: ['./search-manga.component.css']
})
export class SearchMangaComponent implements OnInit {

  public searchTerm : string;
  // construit un tableau de 0 a 5
  public ratingRange : number[] = Array.from({length: 5}, (value, key) => key + 1);
  public currentRating : number;

  private searchTermSubject : Subject<string>;

  constructor(private _mangaRepository : MangaRepositoryService) {
    this.searchTermSubject = new Subject();
   }



  ngOnInit() {
    this.currentRating = 0; // pas de filtrage par rating
    this.searchTerm = "";
    this.searchTermSubject.asObservable()
                          .debounceTime(1000)
                          .subscribe(newTerm => this._mangaRepository.changeSearch(newTerm));
  }

  changeTerm(newvalue) : void {
    this.searchTermSubject.next(newvalue);
    this.searchTerm = newvalue;
  }

  setRatingMin(rating : number) : boolean {
    console.log("change rating : " + rating);
    if (rating != this.currentRating) {
      this._mangaRepository.setFilterByRatingMin(rating);
    }
    this.currentRating = rating;
    return false; // on ne suit pas le lien!
  }
}
