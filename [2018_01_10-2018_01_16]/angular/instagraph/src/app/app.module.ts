import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule} from "@angular/common/http";
import { FormsModule } from "@angular/forms";
import { RouterModule } from "@angular/router";

import { PaginationModule } from 'ngx-bootstrap/pagination';
import { FileUploadModule } from "ng2-file-upload";
import { ProgressbarModule } from 'ngx-bootstrap/progressbar';
import { ModalModule } from 'ngx-bootstrap/modal';

import { LightboxModule } from 'angular2-lightbox';

import { AppComponent } from './app.component';
import { NavBarComponent } from './components/nav-bar/nav-bar.component';
import { ImageListComponent } from './components/image-list/image-list.component';
import { TagSelectorComponent } from './components/tag-selector/tag-selector.component';
import { ImageRepositoryService } from './services/image-repository.service';
import { ImageUploadComponent } from './components/image-upload/image-upload.component';
import { NgStringPipesModule } from "angular-pipes";
import { NgMathPipesModule } from "angular-pipes";


@NgModule({
  declarations: [
    AppComponent,
    NavBarComponent,
    ImageListComponent,
    TagSelectorComponent,
    ImageUploadComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    FileUploadModule,
    LightboxModule,
    NgStringPipesModule,
    NgMathPipesModule,
    PaginationModule.forRoot(),
    ProgressbarModule.forRoot(),
    ModalModule.forRoot(),
    RouterModule.forRoot([
      { path: 'liste', component: ImageListComponent},
      { path: 'upload', component: ImageUploadComponent},
      { path: '', redirectTo: '/liste', pathMatch: 'full'}
    ])
  ],
  providers: [ImageRepositoryService],
  bootstrap: [AppComponent]
})
export class AppModule { }
