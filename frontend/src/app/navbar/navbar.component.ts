import {Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Router} from '@angular/router';
import 'rxjs/add/operator/finally';
import {AppService} from "../services/app-service";

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {

  constructor(private app: AppService, private http: HttpClient, private router: Router) {
  }

  ngOnInit(): void {
  }

  logout() {
    this.http.post('logout', {}).subscribe(() => {
      this.app.authenticated = false;
      this.router.navigateByUrl('/login');
    });
  }
}
