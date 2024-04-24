import {Component, OnInit} from '@angular/core';
import {AuthService} from "./auth/auth.service";
import {CurrencyService} from "./services/currency.service";

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
    this.auth.autoLogin();
    this.currencyService.fetchCurrencies().subscribe(
      data => console.log(data)
    )
  }
}
