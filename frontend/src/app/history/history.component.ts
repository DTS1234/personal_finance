import {Component, OnInit} from '@angular/core';
import {SummaryService} from "../services/summary.service";
import {ChartDataSets, ChartOptions, ChartType} from "chart.js";
import * as Chart from "chart.js";
import {FormControl, FormGroup} from "@angular/forms";
import {SearchCriteria} from "../models/search-criteria.model";
import {CurrencyService} from "../services/currency.service";
import {Summary} from "../models/summary.model";

@Component({
  selector: 'app-history',
  templateUrl: './history.component.html',
  styleUrls: ['./history.component.css']
})
export class HistoryComponent implements OnInit {

  lineChartData: ChartDataSets[] = [
    {data: [], label: 'Historical Data'} // You can define multiple datasets if needed
  ];

  lineChartOptions: ChartOptions = {
    responsive: true,
    // additional options
  };

  lineChartLegend = true;
  lineChartType: ChartType = 'line';
  chart: any = [];

  startDate: Date = new Date(2019, 12)
  endDate: Date = new Date()
  rate = 1.0
  currency = "EUR"

  constructor(private currencyService: CurrencyService, private summaryService: SummaryService) {
  }

  ngOnInit(): void {
    this.currencyService.getCurrency().subscribe(
      currency => {
        this.currency = currency
        this.currencyService.getRateForCurrency("EUR")
          .subscribe(data => {
            this.rate = data
            this.fetchData(0, 10, null)
          })
      }
    )
  }

  fetchData(page: number, size: number, criteria: SearchCriteria) {
    if (criteria == null) {
      criteria = new SearchCriteria(null, null, JSON.parse(localStorage.getItem("userData")).id)
    }
    this.summaryService.querySummaries(criteria, page, size)
      .subscribe(data => {
        console.log(data)
        let x = data.content.map(i => i.date);
        x.unshift("START")
        let y = data.content.map(i => (i.money / this.rate).toFixed(2));
        y.unshift(0)
        this.chart = new Chart('canvas', {
          type: 'line',
          data: {
            labels: x,
            datasets: [
              {
                label: 'Money value over time',
                data: y,
                borderWidth: 1,
              },
            ],
          }
        });
      });
  }

  applyDateFilter() {
    let userId = JSON.parse(localStorage.getItem("userData")).id;
    if (this.startDate == null) {
      this.startDate = new Date(2019, 12)
    }

    if (this.endDate == null) {
      this.endDate = new Date();
    }

    this.fetchData(0, 10, new SearchCriteria(this.startDate, this.endDate, userId))
  }
}
