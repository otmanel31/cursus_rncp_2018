import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from "@angular/router";
import { Manga } from '../../metier/manga';
import { MangaRepositoryService } from '../../services/manga-repository.service';
import { NgForm } from '@angular/forms';


@Component({
  selector: 'app-edit-manga',
  templateUrl: './edit-manga.component.html',
  styleUrls: ['./edit-manga.component.css']
})
export class EditMangaComponent implements OnInit {

  public editedManga : Manga;
  
  constructor(private activeRoute: ActivatedRoute,
              private router: Router,
              private mangaRepository : MangaRepositoryService) {
    
  }

  ngOnInit() {
    this.editedManga = new Manga(0, "", "linus", new Date(), "informatique", 5);
    this.activeRoute.params.subscribe(params => {
      // le parametre ":id" dans la route est disponnible dans
      // le tableau params
      let id= params['id'];
      console.log("param id recu = " + id);
      if (id != 0) {
        this.mangaRepository.findManga(Number(id))
            .then( m => this.editedManga = m);
      }
    });
  }

  sauverTache(monform: NgForm) : void {
    console.log("sauvegarde tache.... : ");
    console.log("valide ? " + monform.valid);
    console.log("dirty ? " + monform.dirty);
    
    console.log(this.editedManga);
    this.mangaRepository.saveManga(this.editedManga)
                        .then(m => {
                          console.log("sauvegarde ok : " + m.id);
                          this.router.navigateByUrl('/liste');
                        })
                        .catch(err => {
                          console.log("erreur a la sauvegarde " + err);
                        });
  }
}
