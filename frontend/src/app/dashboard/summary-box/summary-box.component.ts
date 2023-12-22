import {Component, Input, OnInit} from '@angular/core';
import {Summary} from '../../models/summary.model';
import {DatePipe} from "@angular/common";
import {CurrencyService} from "../../services/currency.service";

@Component({
  selector: 'app-summary-box',
  templateUrl: './summary-box.component.html',
  styleUrls: ['./summary-box.component.css']
})
export class SummaryBoxComponent implements OnInit {

  @Input() summary: Summary;
  currency = "EUR"

  constructor(private datePipe: DatePipe, private currencyService: CurrencyService) {
  }

  ngOnInit(): void {
    this.currencyService.getCurrency().subscribe(data => this.currency = data)
  }

  formatDate(date: string): string {
    return this.datePipe.transform(date, 'dd.MM.yyyy HH:mm:ss');
  }

}
