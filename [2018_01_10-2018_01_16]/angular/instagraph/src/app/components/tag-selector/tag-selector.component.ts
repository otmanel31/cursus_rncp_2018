import { Component, OnInit, Input } from '@angular/core';
import { TagRepositoryService } from '../../services/tag-repository.service';
import { Tag } from '../../models/tag';
import { Subject } from 'rxjs/Subject';
import { Subscription } from 'rxjs/Subscription';
import { Observable } from 'rxjs/Observable';
import { ImageRepositoryService } from '../../services/image-repository.service';

@Component({
  selector: 'app-tag-selector',
  templateUrl: './tag-selector.component.html',
  styleUrls: ['./tag-selector.component.css']
})
export class TagSelectorComponent implements OnInit {

  @Input()
  public tagMode: number = 0;

  constructor(private tagrepository : TagRepositoryService,
              private imageRepository: ImageRepositoryService) { }

  public tagSubject : Subject<Tag[]>;
  public tagSubscription :  Subscription;

  public tagSelected : Observable<Tag[]>;

  ngOnInit() {
    console.log("tag mode = " + this.tagMode);
    this.tagSubject = new Subject<Tag[]>();
    this.tagSubscription = this.tagrepository.listetagAsObservable()
                                            .subscribe( p => {
                                              this.tagSubject.next(p.content);
                                            });
    this.tagrepository.refreshListe();
    this.tagSelected = this.imageRepository.selectedtagsAsObservable();
  }

  public addToSelectedTag(tag: Tag) : void {
    this.imageRepository.addSelectedTag(tag);
  }

  public removeFromSelectedTag(tag: Tag) : void {
    this.imageRepository.removeSelectedTag(tag);
  }

}
