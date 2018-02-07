import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AdminPageModule } from "./modules/admin-page/admin-page.module";
import { RouterModule, Routes } from "@angular/router";

import { AppComponent } from './app.component';
import { HomeComponent } from './components/home/home.component';

const routes: Routes = [
  { path: 'home', component: HomeComponent },
  { path: 'special', 
    loadChildren: "app/modules/special/special.module#SpecialModule"
  },
  { path: '', redirectTo: '/home', pathMatch: 'full' }
];

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent
  ],
  imports: [
    BrowserModule,
    AdminPageModule,
    RouterModule.forRoot(routes)
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
