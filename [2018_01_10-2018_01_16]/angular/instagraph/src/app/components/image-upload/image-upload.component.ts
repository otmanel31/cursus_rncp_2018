import { Component, OnInit } from '@angular/core';
import { FileUploader } from "ng2-file-upload";
import { ImageRepositoryService } from '../../services/image-repository.service';
import { AuthManagerService } from '../../services/auth-manager.service';
import { Tag } from '../../models/tag';

@Component({
  selector: 'app-image-upload',
  templateUrl: './image-upload.component.html',
  styleUrls: ['./image-upload.component.css']
})
export class ImageUploadComponent implements OnInit {

  public tagsToApply : Tag[] = [];

  public uploader: FileUploader;
  public hasBaseDropZoneOver : boolean = false;

  constructor(private imageRepository : ImageRepositoryService,
              private authManager : AuthManagerService) {
    this.uploader = new FileUploader({
      autoUpload : true,
      url: this.imageRepository.getUploadurl() /*,
      authTokenHeader: 'Authorization',
      authToken: `Basic ${this.authManager.getCredentials()}`*/
    });
  }

  ngOnInit() {
  }

  public fileOverDrop(event) : void {
    console.log("fileover: " + event);
    this.hasBaseDropZoneOver = event;
  }


  private refreshUploader() : void {
    let newurl = this.imageRepository.getUploadurl();
    if (this.tagsToApply.length > 0) {
      newurl += `?tagIds=${this.tagsToApply.map(t => t.id).join(",")}`;
      this.uploader.setOptions({url: newurl});
    }
  }

  public selectNewTag(tag : Tag) : void {
    if (this.tagsToApply.findIndex(t => t.id == tag.id) == -1) {
      this.tagsToApply.push(tag);
      this.refreshUploader();
    }
  }

  public unSelectTag(tag : Tag) : void {
    let index = this.tagsToApply.findIndex(t => t.id == tag.id);
    if  (index != -1) {
      this.tagsToApply.splice(index, 1);
      this.refreshUploader();
    }
  }


}
