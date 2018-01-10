import { Component, OnInit } from '@angular/core';
import { ImageRepositoryService } from '../../services/image-repository.service';

import { Observable } from 'rxjs/Observable';
import { Subject } from 'rxjs/Subject';
import { Subscription } from 'rxjs/Subscription';
import { Image } from '../../models/image';
import { OnDestroy } from '@angular/core/src/metadata/lifecycle_hooks';


@Component({
  selector: 'app-image-list',
  templateUrl: './image-list.component.html',
  styleUrls: ['./image-list.component.css']
})
export class ImageListComponent implements OnInit, OnDestroy {
 
  private souscription : Subscription;
  public images : Subject<Image[]>;
  public totalItems : number = 0;
  public currentPage : number = 1;

  constructor(private imageRepository : ImageRepositoryService) { }

  public getImageThumbUrl(id: number) : string {
    return this.imageRepository.getImageThumbUrl(id);
  }
  public getImageUrl(id: number) : string {
    return this.imageRepository.getImageUrl(id);
  }

  public pageChanged(event : any) : void {
    this.imageRepository.setNoPage(event.page - 1);
  }

  ngOnInit() {
    this.images = new Subject();
    this.souscription = this.imageRepository.listeImageAsObservable()
                            .subscribe(p =>  {
                              this.images.next(p.content);
                              this.totalItems = p.totalElements;
                              this.currentPage = p.number + 1;
                            });
    this.imageRepository.refreshListe();
  }

  ngOnDestroy(): void {
    this.souscription.unsubscribe();
  }

}
