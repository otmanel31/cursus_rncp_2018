import { Component, OnInit } from '@angular/core';
import { ImageRepositoryService } from '../../services/image-repository.service';
import { Router, ActivatedRoute } from '@angular/router';
import { Image } from '../../models/image';
import { AlertManagerService } from '../../services/alert-manager.service';

@Component({
  selector: 'app-image-edit',
  templateUrl: './image-edit.component.html',
  styleUrls: ['./image-edit.component.css']
})
export class ImageEditComponent implements OnInit {

  public editedImage : any|Image;

  constructor(private imageRepo : ImageRepositoryService,
              private router : Router,
              private activeroute : ActivatedRoute,
              private alertManager : AlertManagerService) { }

  ngOnInit() {
    this.editedImage = {};
    this.activeroute.params.subscribe( params => {
      let id = Number(params["id"]);
      console.log("id image = " + id);
      this.imageRepo.findImage(id)
                    .then( img => this.editedImage = img)
                    .catch( err => {
                       this.alertManager.handleErrorResponse(err);
                       this.router.navigateByUrl("/liste");
                    });
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

}
