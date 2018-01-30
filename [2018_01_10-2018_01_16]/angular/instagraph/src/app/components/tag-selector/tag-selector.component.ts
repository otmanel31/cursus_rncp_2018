import { Component, OnInit, Input, Output, EventEmitter, TemplateRef } from '@angular/core';
import { TagRepositoryService } from '../../services/tag-repository.service';
import { Tag } from '../../models/tag';
import { Subject } from 'rxjs/Subject';
import { Subscription } from 'rxjs/Subscription';
import { Observable } from 'rxjs/Observable';
import { ImageRepositoryService } from '../../services/image-repository.service';
import { BsModalRef } from 'ngx-bootstrap/modal';

import "rxjs/add/operator/debounceTime";
import { BsModalService } from 'ngx-bootstrap/modal/bs-modal.service';
import { AlertManagerService } from '../../services/alert-manager.service';


@Component({
  selector: 'app-tag-selector',
  templateUrl: './tag-selector.component.html',
  styleUrls: ['./tag-selector.component.css']
})
export class TagSelectorComponent implements OnInit {
  modalRef: BsModalRef;
  public createdTag : Tag;

  // mode 0 -> liste/filtrage
  // mode 1 -> edition/image
  @Input()
  public tagMode: number = 0;
  // liste des tags associés au contenu edité
  @Input()
  public relatedTags : Tag[] = [];

  // prevenir composant parent qu'un tag est choisi
  @Output()
  public tagAddChange : EventEmitter<Tag> = new EventEmitter<Tag>();
  @Output()
  public tagRemoveChange : EventEmitter<Tag> = new EventEmitter<Tag>();

  public searchTerm : string = "";

  constructor(private tagrepository : TagRepositoryService,
              private imageRepository: ImageRepositoryService,
              private modalService : BsModalService,
              private alertManager : AlertManagerService) { }

  public tagSubject : Subject<Tag[]>;
  public tagSubscription :  Subscription;

  // tags utilisé pour filtrage
  public tagSelected : Observable<[boolean,Tag][]>;

  // sujet pour la recherche
  public searchSubject : Subject<string>;

  ngOnInit() {
    console.log("tag mode = " + this.tagMode);
    this.tagSubject = new Subject<Tag[]>();
    this.tagSubscription = this.tagrepository.listetagAsObservable()
                                            .subscribe( p => {
                                              this.tagSubject.next(p.content);
                                            });
    this.tagrepository.refreshListe();
    this.tagSelected = this.imageRepository.selectedtagsAsObservable();
    this.searchSubject = new Subject<string>();
    this.searchSubject.asObservable()
                      .debounceTime(500)
                      .subscribe( term => this.tagrepository.setSearchTerm(term));
    this.createdTag = new Tag(0, "", "");
  }

  public addToSelectedTag(tag: Tag) : void {
    //this.imageRepository.addSelectedTag(tag);
    this.tagAddChange.emit(tag);
  }

  public removeFromSelectedTag(tag: Tag) : void {
    //this.imageRepository.removeSelectedTag(tag);
    this.tagRemoveChange.emit(tag);
  }

  public toggleSelectedTag(tag : [boolean, Tag]) : void {
    tag[0] = !tag[0]; // switch negative/positive
    this.imageRepository.updateSelectedTag(tag);
  }

  public changeSearch(term : string) : void {
    this.searchTerm = term;
    this.searchSubject.next(term);
  }

  public createTag(createTemplate : TemplateRef<any>) : void {
    console.log(this.searchTerm);
    this.createdTag.libelle = this.searchTerm;
    this.modalRef = this.modalService.show(createTemplate);
  }

  public confirmCreate() : void {
    this.modalRef.hide();
    this.tagrepository.createTag(this.createdTag)
                      .then( t => {
                        this.alertManager.handleMessage("success", `tag ${t.libelle} created`);
                        this.tagrepository.refreshListe();
                      })
                      .catch (err => {
                        this.alertManager.handleErrorResponse(err);
                      });
  }
  
}
