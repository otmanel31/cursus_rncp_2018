import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule} from "@angular/common/http";
import { FormsModule } from "@angular/forms";
import { RouterModule } from "@angular/router";

import { PaginationModule } from 'ngx-bootstrap/pagination';

import { AppComponent } from './app.component';
import { MangaRepositoryService } from './services/manga-repository.service';
import { SearchMangaComponent } from './components/search-manga/search-manga.component';
import { ListeMangaComponent } from './components/liste-manga/liste-manga.component';
import { EditMangaComponent } from './components/edit-manga/edit-manga.component';


@NgModule({
  declarations: [
    AppComponent,
    SearchMangaComponent,
    ListeMangaComponent,
    EditMangaComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    PaginationModule.forRoot(),
    RouterModule.forRoot([
      {path: 'liste', component: ListeMangaComponent},
      {path: 'edit/:id', component: EditMangaComponent},
      {path: '', redirectTo: '/liste', pathMatch: 'full'}
    ])
  ],
  providers: [MangaRepositoryService],
  bootstrap: [AppComponent]
})
export class AppModule { }
