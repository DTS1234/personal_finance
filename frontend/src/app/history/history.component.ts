import {Component, OnInit} from '@angular/core';
import {SummaryService} from "../services/summary.service";
import {ChartConfiguration, ChartOptions, ChartType} from "chart.js";
import Chart from "chart.js/auto";
import {SearchCriteria} from "../models/search-criteria.model";
import {CurrencyService} from "../services/currency.service";
import {Summary} from "../models/summary.model";

@Component({
  selector: 'app-history',
  templateUrl: './history.component.html',
  styleUrls: ['./history.component.css']
})
export class HistoryComponent implements OnInit {
  lineChartOptions: ChartOptions = {
    responsive: true,
    // additional options like tooltips, scales, etc.
    scales: {
      x: {
        title: {
          display: true,
          text: 'Date'
        }
      },
      y: {
        title: {
          display: true,
          text: 'Money Value'
        }
      }
    }
  };

  lineChartType: ChartType = 'line';
  chart: Chart | undefined;

  startDate: Date = new Date(2019, 12);
  endDate: Date = new Date();
  rate = 1.0;
  currency = "EUR";

  constructor(private currencyService: CurrencyService, private summaryService: SummaryService) {
  }

  ngOnInit(): void {
    this.currencyService.getCurrency().subscribe(
      currency => {
        this.currency = currency;
        this.currencyService.getRateForCurrency(this.currency)
          .subscribe(data => {
            this.rate = data;
            this.fetchData(0, 10, null);
          });
      }
    );
  }

  fetchData(page: number, size: number, criteria: SearchCriteria | null) {
    if (criteria == null) {
      criteria = new SearchCriteria(null, null, JSON.parse(localStorage.getItem("userData")!).id);
    }
    this.summaryService.querySummaries(criteria, page, size)
      .subscribe(data => {
        console.log(data);
        let labels = data.content.map((i: Summary) => i.date);
        labels.unshift("START");

        let values = data.content.map((i: Summary) => (i.money / this.rate).toFixed(2));
        values.unshift('0');

        if (this.chart) {
          this.chart.destroy();
        }

        this.chart = new Chart('canvas', {
          type: 'line',
          data: {
            labels: labels,
            datasets: [
              {
                label: 'Money value over time',
                data: values,
                borderColor: '#3e95cd',
                borderWidth: 2,
                fill: false,
                tension: 0.1 // Adds smooth curves to the line chart
              },
            ],
          },
          options: this.lineChartOptions
        });
      });
  }

  applyDateFilter() {
    let userId = JSON.parse(localStorage.getItem("userData")!).id;
    if (this.startDate == null) {
      this.startDate = new Date(2019, 12);
    }

    if (this.endDate == null) {
      this.endDate = new Date();
    }

    this.fetchData(0, 10, new SearchCriteria(this.startDate, this.endDate, userId));
  }
}
