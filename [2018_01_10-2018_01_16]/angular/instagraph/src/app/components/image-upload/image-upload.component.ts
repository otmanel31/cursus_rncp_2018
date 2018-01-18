import { Component, OnInit } from '@angular/core';
import { FileUploader } from "ng2-file-upload";
import { ImageRepositoryService } from '../../services/image-repository.service';
import { AuthManagerService } from '../../services/auth-manager.service';

@Component({
  selector: 'app-image-upload',
  templateUrl: './image-upload.component.html',
  styleUrls: ['./image-upload.component.css']
})
export class ImageUploadComponent implements OnInit {

  public uploader: FileUploader;
  public hasBaseDropZoneOver : boolean = false;

  constructor(private imageRepository : ImageRepositoryService,
              private authManager : AuthManagerService) {
    this.uploader = new FileUploader({
      autoUpload : true,
      url: this.imageRepository.getUploadurl(),
      authTokenHeader: 'Authorization',
      authToken: `Basic ${this.authManager.getCredentials()}`
    });
  }

  ngOnInit() {
  }

  public fileOverDrop(event) : void {
    console.log("fileover: " + event);
    this.hasBaseDropZoneOver = event;
  }

}
