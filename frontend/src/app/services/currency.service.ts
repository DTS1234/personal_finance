// currency.service.ts
import {Injectable, OnInit} from '@angular/core';
import {BehaviorSubject, Observable} from 'rxjs';
import {HttpClient} from "@angular/common/http";
import {UpdateCurrency} from "../models/update-currency.model";
import {CurrenciesResponse} from "../models/currencies-rates.model";
import {map, tap} from "rxjs/operators";
import {Summary} from "../models/summary.model";

@Injectable({
  providedIn: 'root'
})
export class CurrencyService {
  private currentCurrency = new BehaviorSubject<string>('EUR'); // Default currency
  private basePath = 'http://localhost:8080';
  private currencyRates = new BehaviorSubject<Record<string, number>>({});

  constructor(private http: HttpClient) {
  }

  setCurrency(currency: string): void {
    this.currentCurrency.next(currency);
  }

  getCurrency(): Observable<string> {
    return this.currentCurrency.asObservable();
  }

  updateCurrency(currency: string): Observable<string> {
    let userId = JSON.parse(localStorage.getItem("userData")).id;
    return this.http.post<string>(`${this.basePath}/${userId}/currency`, new UpdateCurrency(userId, currency));
  }

  fetchCurrencies() {
    return this.http.get<CurrenciesResponse>(`${this.basePath}/currencies`).pipe(
      tap(response => {
        this.currencyRates.next(response.rates);
        console.log("CURRENCIES fetched: " + this.currencyRates.value)
      })
    );
  }

  getCurrencyRates(): Observable<Record<string, number>> {
    return this.currencyRates.asObservable();
  }

  getRateForCurrency(currency: string) {
    return this.getRate(new Summary("", 'date', 0, currency, []))
  }

  getRate(summary: Summary): Observable<number> {

    return this.getCurrency().pipe(map(currency => {
        console.log("CURRENCY " + currency)

        if (currency == null) {
          this.currentCurrency.next("EUR")
        }

        const summaryCurrency = summary.currency
        if (currency == summaryCurrency) {
          return 1.00
        }

        let currencyRate = this.currencyRates.value[`(${summaryCurrency},${currency})`];
        console.log("CURRENCY RATE FROM METHOD: " + currencyRate)
        return currencyRate
      })
    )

  }

}
