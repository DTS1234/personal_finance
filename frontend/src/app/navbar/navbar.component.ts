import {Component, OnDestroy, OnInit} from '@angular/core';
import {AuthService} from "../services/auth.service";
import {Subscription} from "rxjs";
import {Router} from "@angular/router";
import {CurrencyService} from "../services/currency.service";

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit, OnDestroy {
  isAuthenticated = false
  private userSub: Subscription

  constructor(private authService: AuthService, private currencyService: CurrencyService, private router: Router) {
  }

  ngOnInit(): void {
    this.userSub = this.authService.user.subscribe(user => {
      this.isAuthenticated = !!user
    });
  }

  ngOnDestroy(): void {
    this.userSub.unsubscribe()
  }

  onLogout() {
    this.authService.logout()
    this.router.navigate(['/homepage'])
  }

  onCurrencyChange($event: Event) {
    const selectedCurrency = (event.target as HTMLSelectElement).value;
    this.currencyService.setCurrency(selectedCurrency);
    this.currencyService.updateCurrency(selectedCurrency).subscribe(
      data => console.log("CURRENCY CHANGE TO " + data)
    )
  }
}
