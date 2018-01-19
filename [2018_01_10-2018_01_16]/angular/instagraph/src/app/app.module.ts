import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule, HTTP_INTERCEPTORS} from "@angular/common/http";
import { FormsModule } from "@angular/forms";
import { RouterModule } from "@angular/router";

import { PaginationModule } from 'ngx-bootstrap/pagination';
import { FileUploadModule } from "ng2-file-upload";
import { ProgressbarModule } from 'ngx-bootstrap/progressbar';
import { ModalModule } from 'ngx-bootstrap/modal';
import { PopoverModule } from "ngx-bootstrap/popover";
import { AlertModule } from "ngx-bootstrap/alert";

import { LightboxModule } from 'angular2-lightbox';

import { AppComponent } from './app.component';
import { NavBarComponent } from './components/nav-bar/nav-bar.component';
import { ImageListComponent } from './components/image-list/image-list.component';
import { TagSelectorComponent } from './components/tag-selector/tag-selector.component';
import { ImageRepositoryService } from './services/image-repository.service';
import { ImageUploadComponent } from './components/image-upload/image-upload.component';
import { NgStringPipesModule } from "angular-pipes";
import { NgMathPipesModule } from "angular-pipes";
import { TagRepositoryService } from './services/tag-repository.service';
import { AuthInterceptorService } from "./services/auth-interceptor.service";
import { LoginComponent } from './components/login/login.component';
import { AuthManagerService } from './services/auth-manager.service';
import { UtilisateurInfoComponent } from './components/utilisateur-info/utilisateur-info.component';
import { RegisterUserComponent } from './components/register-user/register-user.component';
import { AlertDisplayComponent } from './components/alert-display/alert-display.component';
import { AlertManagerService } from './services/alert-manager.service';

@NgModule({
  declarations: [
    AppComponent,
    NavBarComponent,
    ImageListComponent,
    TagSelectorComponent,
    ImageUploadComponent,
    LoginComponent,
    UtilisateurInfoComponent,
    RegisterUserComponent,
    AlertDisplayComponent
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
    PopoverModule.forRoot(),
    AlertModule.forRoot(),
    RouterModule.forRoot([
      { path: 'liste', component: ImageListComponent},
      { path: 'upload', component: ImageUploadComponent},
      { path: 'login', component: LoginComponent},
      { path: 'register', component: RegisterUserComponent},
      { path: '', redirectTo: '/liste', pathMatch: 'full'}
    ])
  ],
  providers: [
    ImageRepositoryService,
    TagRepositoryService,
    AlertManagerService,
    AuthManagerService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptorService,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
