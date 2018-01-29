import { Component, OnInit } from '@angular/core';
import { ImageRepositoryService } from '../../services/image-repository.service';
import { Router, ActivatedRoute } from '@angular/router';
import { Image } from '../../models/image';
import { AlertManagerService } from '../../services/alert-manager.service';
import { Tag } from '../../models/tag';
import { TagRepositoryService } from '../../services/tag-repository.service';

@Component({
  selector: 'app-image-edit',
  templateUrl: './image-edit.component.html',
  styleUrls: ['./image-edit.component.css']
})
export class ImageEditComponent implements OnInit {

  public editedImage : any|Image;
  public tagsRelated : Tag[] = [];

  constructor(private imageRepo : ImageRepositoryService,
              private tagRepo : TagRepositoryService,
              private router : Router,
              private activeroute : ActivatedRoute,
              private alertManager : AlertManagerService) { }

  ngOnInit() {
    this.editedImage = {};
    this.activeroute.params.subscribe( params => {
      let id = Number(params["id"]);
      console.log("id image = " + id);
      this.imageRepo.findImage(id)
                    .then( img => {
                      this.editedImage = img;
                      this.refreshTags();
                    })
                    .catch( err => {
                       this.alertManager.handleErrorResponse(err);
                       this.router.navigateByUrl("/liste");
                    });
    });
  }

  public refreshTags() : void {
    this.tagRepo.getRelatedTags(this.editedImage.id)
        .then(tags => {
          this.tagsRelated = tags;
        })
        .catch( err => {
          this.alertManager.handleErrorResponse(err);
        });
  }

  public saveImage() : void {
    this.imageRepo.updateImage(this.editedImage)
        .then( img => {
              this.alertManager.handleMessage("success", `image '${img.name}' saved`);
              this.router.navigateByUrl("/liste");
            })
        .catch( err => {
          this.alertManager.handleErrorResponse(err);
        });
  }

  // ajouter tag au filtrage
  public selectNewTag(tag: Tag) : void {
    console.log("ajout tag " + tag.libelle + " a l'image"); 
    this.tagRepo.addTags([tag], [this.editedImage])
                .then( tags => {
                  this.refreshTags();
                  this.alertManager.handleMessage("success", `tag '${tags[0].libelle}' added`);
                })
                .catch( err => {
                  this.alertManager.handleErrorResponse(err);
                });
  }
  // ajouter tag au filtrage
  public unSelectTag(tag: Tag) : void {
    console.log("retrait tag " + tag.libelle + " de l'image");
    this.tagRepo.removeTags([tag], [this.editedImage])
                .then( tags => {
                  this.refreshTags();
                  this.alertManager.handleMessage("success", `tag '${tags[0].libelle}' removed`);
                })
                .catch( err => {
                  this.alertManager.handleErrorResponse(err);
                });

  }

  public getImageThumbUrl() : string {
    return this.imageRepo.getImageThumbUrl(this.editedImage.id);
  }
}
