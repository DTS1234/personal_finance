import {Component, OnInit} from '@angular/core';
import {AuthService} from "./services/auth.service";
import {CurrencyService} from "./services/currency.service";
import {log} from "@angular-devkit/build-angular/src/builders/ssr-dev-server";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'frontend';

  constructor(private auth: AuthService, private currencyService: CurrencyService) {
  }

  ngOnInit(): void {
    console.log("APP COMONENT ON INIT!!!!")
    this.auth.autoLogin();
    this.currencyService.fetchCurrencies().subscribe(
      data => console.log(data)
    )
  }
}
