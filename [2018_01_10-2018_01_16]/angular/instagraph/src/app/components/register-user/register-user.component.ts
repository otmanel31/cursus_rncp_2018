import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Router } from '@angular/router';
import { Utilisateur } from '../../models/utilisateur';

@Component({
  selector: 'app-register-user',
  templateUrl: './register-user.component.html',
  styleUrls: ['./register-user.component.css']
})
export class RegisterUserComponent implements OnInit {
  public userregister : any;
  private baseUrlApi: string = "http://localhost:8080/extendedapi/auth";

  constructor(private http: HttpClient, private router : Router) { }

  
  ngOnInit() {
    this.userregister = {username : "bob", password: ""};

  }

  public tryregister() : void {
    console.log("try register with " + this.userregister.username);
    let params : HttpParams  = new HttpParams().set("username", this.userregister.username)
                                               .set("password", this.userregister.password);
    this.http.post<Utilisateur>(`${this.baseUrlApi}/register`, {}, {params: params})
             .subscribe(u => {
               console.log("utilisateur cree: " + u.username);
               this.router.navigateByUrl("/login");
             }, e => {
               console.log("register failed " + e);
             });
  }
}
