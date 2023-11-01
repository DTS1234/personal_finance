import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {HttpClient} from "@angular/common/http";
import {AppService} from "../services/app-service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  credentials = {username: '', password: ''};

  constructor(private app: AppService, private http: HttpClient, private router: Router) {
  }

  login(): boolean {
    this.app.authenticate(this.credentials, () => {
      this.router.navigateByUrl('/');
    });
    return false;
  }

}
