import {Component, OnInit} from '@angular/core';
import {ExchangeDataService} from "./exchange-data.service";
import {ExchangeData} from "./exchange-data.model";
import {FormsModule} from "@angular/forms";
import {DecimalPipe, NgForOf, NgIf} from "@angular/common";
import {TickerData} from "./ticker-data.model";
import {StockData} from "./stock-data.model";

@Component({
  selector: 'app-explorer',
  standalone: true,
  imports: [
    FormsModule,
    NgForOf,
    NgIf,
    DecimalPipe
  ],
  templateUrl: './explorer.component.html',
  styleUrl: './explorer.component.css'
})
export class ExplorerComponent implements OnInit {

  exchanges: ExchangeData[] = []
  filteredExchanges: ExchangeData[] = []
  searchText: String
  searchTickerText: String
  tickers: TickerData[] = [];
  filteredTickers: TickerData[] = []
  selectedTickerIndex: number;

  stock: StockData = null;

  constructor(private exchangeDataService: ExchangeDataService) {

  }

  ngOnInit(): void {
    this.exchangeDataService.getExchangeData().subscribe(
      data => {
        this.exchanges = data;
        this.filteredExchanges = data;
      }
    );
  }

  selectExchange(exchange: ExchangeData) {
    this.filteredExchanges = [exchange];
    this.exchangeDataService.getTickers(exchange.Code).subscribe(data => {
      this.tickers = data
      this.filteredTickers = data
    });
  }

  filterExchanges() {
    if (!this.searchText) {
      this.filteredExchanges = this.exchanges;
    } else {
      this.filteredExchanges = this.exchanges.filter(ex =>
        ex.Name.toLowerCase().includes(this.searchText.toLowerCase()) ||
        ex.Code.toLowerCase().includes(this.searchText.toLowerCase()) ||
        ex.Country.toLowerCase().includes(this.searchText.toLowerCase())
      );
      this.filteredTickers = []
    }
  }

  filterTickers() {
    if (!this.searchTickerText) {
      this.filteredTickers = this.tickers
    } else {
      this.filteredTickers = this.tickers.filter(ex =>
        ex.Name.toLowerCase().includes(this.searchTickerText.toLowerCase()) ||
        ex.Code.toLowerCase().includes(this.searchTickerText.toLowerCase()) ||
        ex.Country.toLowerCase().includes(this.searchTickerText.toLowerCase())
      );
    }
  }

  selectTicker(i: number) {
    let selected = this.filteredTickers[i];
    let exchangeCode = selected.Exchange
    if (selected.Country == "USA") {
      exchangeCode = "US"
    }
    this.exchangeDataService.getStockData(exchangeCode, selected.Code)
      .subscribe(data => {
        console.log(data)
        this.stock = data[0]
        this.filteredTickers = [selected]
      });
  }
}
