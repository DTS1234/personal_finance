// stock-item-form.component.ts
import {Component, Input} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule} from '@angular/forms';
import {ExchangeDataService} from "../../../explorer/exchange-data.service";
import {SearchStockData} from "./search-stock-item.model";
import {NgForOf} from "@angular/common";

@Component({
  selector: 'app-stock-item-form',
  templateUrl: './stock-item-form.component.html',
  styleUrls: ['./stock-item-form.component.css'],
  imports: [
    ReactiveFormsModule,
    NgForOf,
    FormsModule
  ],
  standalone: true
})
export class StockItemFormComponent {
  @Input() assetForm!: FormGroup;

  query: FormControl = new FormControl('')
  found: SearchStockData[] = []

  constructor(private exchangeDataService: ExchangeDataService) {
  }

  search() {
    this.exchangeDataService.getBySearch(this.query.value)
      .subscribe(data => this.found = data);
  }

  selectTicker() {
    const selectedTickerCode = this.assetForm.get('ticker')?.value;
    const selectedTicker = this.found.find(ticker => ticker.Name === selectedTickerCode);
    this.assetForm.get("currentPrice").setValue(selectedTicker.previousClose)
  }
}
