import {Component, OnInit} from '@angular/core';
import {AssetService} from './services/asset.service';
import {AuthService} from "./services/auth.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{
  title = 'frontend';

  constructor(private auth: AuthService) {
  }

  ngOnInit(): void {
    this.auth.autoLogin();
  }


}
