import { Component, OnInit } from '@angular/core';
import { TagRepositoryService } from '../../services/tag-repository.service';
import { Tag } from '../../models/tag';
import { Subject } from 'rxjs/Subject';
import { Subscription } from 'rxjs/Subscription';

@Component({
  selector: 'app-tag-selector',
  templateUrl: './tag-selector.component.html',
  styleUrls: ['./tag-selector.component.css']
})
export class TagSelectorComponent implements OnInit {

  constructor(private tagrepository : TagRepositoryService) { }

  public tagSubject : Subject<Tag[]>;
  public tagSubscription :  Subscription;

  ngOnInit() {
    this.tagSubject = new Subject<Tag[]>();
    this.tagSubscription = this.tagrepository.listetagAsObservable()
                                            .subscribe( p => {
                                              this.tagSubject.next(p.content);
                                            });
    this.tagrepository.refreshListe();
  }

}
