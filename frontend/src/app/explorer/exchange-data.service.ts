import {HttpClient} from "@angular/common/http";
import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {ExchangeData} from "./exchange-data.model";
import {TickerData} from "./ticker-data.model";
import {StockData} from "./stock-data.model";

@Injectable({
  providedIn: 'root'
})
export class ExchangeDataService {
  private apiUrl = 'http://localhost:8080/exchange_list'; // Replace with your actual API URL

  constructor(private http: HttpClient) {
  }

  getExchangeData(): Observable<ExchangeData[]> {
    return this.http.get<ExchangeData[]>(this.apiUrl);
  }

  getTickers(exchange: String): Observable<TickerData[]> {
    return this.http.get<TickerData[]>(this.apiUrl + "/" + exchange + "/stocks");
  }

  getStockData(exchange: string, stock: string): Observable<StockData[]> {
    return this.http.get<StockData[]>('http://localhost:8080/' + exchange + "/" + stock)
  }
}
