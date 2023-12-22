// currency.service.ts
import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable} from 'rxjs';
import {HttpClient} from "@angular/common/http";
import {UpdateCurrency} from "../models/update-currency.model";

@Injectable({
  providedIn: 'root'
})
export class CurrencyService {
  private currentCurrency = new BehaviorSubject<string>('EUR'); // Default currency
  private basePath = 'http://localhost:8080';

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
}
