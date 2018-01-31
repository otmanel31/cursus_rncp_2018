import { Component, OnInit, TemplateRef } from '@angular/core';
import { ImageRepositoryService } from '../../services/image-repository.service';

import { Observable } from 'rxjs/Observable';
import { Subject } from 'rxjs/Subject';
import { Subscription } from 'rxjs/Subscription';
import { Image } from '../../models/image';
import { OnDestroy } from '@angular/core/src/metadata/lifecycle_hooks';

import { BsModalService } from 'ngx-bootstrap/modal';
import { BsModalRef } from 'ngx-bootstrap/modal/bs-modal-ref.service';
import { Lightbox, IAlbum } from 'angular2-lightbox';
import { TruncatePipe } from "angular-pipes/src/string/truncate.pipe";
import { BytesPipe } from "angular-pipes/src/math/bytes.pipe";
import { AuthManagerService } from '../../services/auth-manager.service';
import { AlertManagerService } from '../../services/alert-manager.service';
import { Tag } from '../../models/tag';
import { TagRepositoryService } from '../../services/tag-repository.service';



@Component({
  selector: 'app-image-list',
  templateUrl: './image-list.component.html',
  styleUrls: ['./image-list.component.css']
})
export class ImageListComponent implements OnInit, OnDestroy {
  modalRef: BsModalRef;

  private souscription : Subscription;
  public images : Subject<Image[]>;
  //pagination
  public totalItems : number = 0;
  public currentPage : number = 1;
  // gallerie
  public gallerieLiens : any[] = [];
  // selection
  public idsToDelete : number[] = [];
  public selectedImages : Image[];
  public currentPageImages : Image[];
  public messageDelete : string;
  public currentTag : Tag;

  constructor(private imageRepository : ImageRepositoryService,
              private tagRepository : TagRepositoryService,
              private modalService : BsModalService,
              private lightBox : Lightbox,
              private authManager: AuthManagerService,
              private alertManager: AlertManagerService) { }

  public getImageThumbUrl(id: number) : string {
    return this.imageRepository.getImageThumbUrl(id);
  }
  public getImageUrl(id: number) : string {
    return this.imageRepository.getImageUrl(id);
  }
  public getImagePopovertext(image : Image) : string {
    if( image.tags == null || image.tags.length == 0) {
      return "aucun tags";
    }
    else {
      return "tags: " + image.tags.map(t => t.libelle).join(",");
    }
  }

  public pageChanged(event : any) : void {
    this.imageRepository.setNoPage(event.page - 1);
  }

  // ai-je le droit de supprimer ?
  public canDelete() : boolean {
    return true;
    //console.log("canDelete " + this.authManager.isRoleActive("ROLE_ADMIN"));
    //return this.authManager.isRoleActive("ROLE_ADMIN") || this.authManager.isRoleActive("ROLE_USER");
  }

  // affiche le dialogue
  public deleteImage(id : number, deleteTemplate : TemplateRef<any>) : void {
    console.log("effacement image no " + id + " demandé");
    this.idsToDelete = [id]; // on memorise l'id de l'image a effacer
    this.messageDelete = "effacer image no " + id; 
    this.modalRef = this.modalService.show(deleteTemplate);
  }

// affiche le dialogue
public deleteSelectedImages(deleteTemplate : TemplateRef<any>) : void {
  this.idsToDelete = this.selectedImages.map(img => img.id);
  this.messageDelete = "effacer image des images " + this.idsToDelete.join(","); 
  this.modalRef = this.modalService.show(deleteTemplate);
}


  // appelé si on clique sur ok dans le dialogue du dessus
  public confirmDelete() : void {
    this.modalRef.hide();
    //console.log("effacement image no " + this.idToDelete + " confirmé");
    this.imageRepository.deleteImages(this.idsToDelete);
  }

  public tagImageSelection(tag : Tag, templateRef: TemplateRef<any>) : void {
    if (this.selectedImages.length == 0) return;
    console.log("tag selection with " + tag.libelle);
    this.currentTag = tag;
    this.modalRef = this.modalService.show(templateRef);
  }
  public confirmAddTag() : void {
    this.modalRef.hide();
    this.tagRepository.addTags([this.currentTag], this.selectedImages)
                      .then(tags => {
                        this.alertManager.handleMessage("success", `${tags[0].libelle} tag added`);
                        this.imageRepository.refreshListe();
                      })
                      .catch( err => {
                        this.alertManager.handleErrorResponse(err);
                      });
  }

  public confirmRemoveTag() : void {
    this.modalRef.hide();
    this.tagRepository.removeTags([this.currentTag], this.selectedImages)
                      .then(tags => {
                        this.alertManager.handleMessage("success", `${tags[0].libelle} tag removed`);
                        this.imageRepository.refreshListe();
                      })
                      .catch( err => {
                        this.alertManager.handleErrorResponse(err);
                      });
  }
  



  public openGallerie(image: Image) : void {
    /*let album = [];
    album.push({
      src: this.imageRepository.getImageUrl(image.id),
      caption: image.fileName
    });*/
    // quelle est la position de l'image cliqué dans l'album
    let position = this.gallerieLiens.findIndex(imglien => imglien.id == image.id);
    // ouvrir l'album positionné sur la bonne image
    this.lightBox.open(this.gallerieLiens,
                       position,
                      {
                        fadeDuration: 0.2,
                        resizeDuration: 0.2,
                        showImageNumberLabel : true,
                        wrapAround: true
                      });
  }

  // ajouter tag au filtrage
  public selectNewTag(tag: Tag) : void {
    this.imageRepository.addSelectedTag(tag);
  }
// ajouter tag au filtrage
  public unSelectTag(tag: Tag) : void {
    this.imageRepository.removeSelectedTag(tag);
  }

  public isSelected(image: Image) : boolean {
    return this.selectedImages.findIndex(img => img.id == image.id) != -1;
  }

  public toggleSelect(image: Image) : void {
    let index = this.selectedImages.findIndex(img => img.id == image.id);
    if (index == -1) {
      // ajouter si non présente
      this.selectedImages.push(image);
    }
    else {
      // retirer si déjà présente
      this.selectedImages.splice(index, 1);
    }

  }

  public addCurrentPageToSelection() : void {
    this.currentPageImages.forEach( img => {
      if (this.selectedImages.findIndex(img2 => img2.id == img.id) == -1) {
        this.selectedImages.push(img);
      }
    });
  }


  public openSelectedGallerie() : void {
    let liens = [];
    this.selectedImages.forEach(img => {
      liens.push({id: img.id, src: this.imageRepository.getImageUrl(img.id), caption: img.fileName});
    })
    this.lightBox.open(liens,
                        0,
                      {
                        fadeDuration: 0.2,
                        resizeDuration: 0.2,
                        showImageNumberLabel : true,
                        wrapAround: true
                      });
  }

  ngOnInit() {
    this.images = new Subject();
    this.souscription = this.imageRepository.listeImageAsObservable()
                            .subscribe(p =>  {
                              // mettre a jour liens pour lightbox
                              this.gallerieLiens = [];
                              this.currentPageImages = p.content;
                              p.content.forEach(img => {
                                this.gallerieLiens.push({
                                  id : img.id,
                                  src: this.imageRepository.getImageUrl(img.id),
                                  caption : img.fileName
                                });
                              });
                              // publier les images pour le ngFor
                              this.images.next(p.content);
                              this.totalItems = p.totalElements;
                              this.currentPage = p.number + 1;
                            });
    // aucune image séléctionnées
    this.selectedImages = [];
    this.imageRepository.refreshListe();
  }
  public clearSelection() {
    this.selectedImages = [];
  }


  ngOnDestroy(): void {
    this.souscription.unsubscribe();
  }

}
