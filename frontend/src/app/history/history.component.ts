import {Component, OnInit} from '@angular/core';
import {SummaryService} from "../services/summary.service";
import {ChartDataSets, ChartOptions, ChartType} from "chart.js";
import * as Chart from "chart.js";

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

  constructor(private summaryService: SummaryService) {
  }

  ngOnInit(): void {
    this.fetchData(0, 10);
  }


  fetchData(page: number, size: number) {
    this.summaryService.querySummaries({/* criteria */}, page, size)
      .subscribe(data => {
        console.log(data)
        let x = data.content.map(i => i.date);
        x.unshift("START")
        let y = data.content.map(i => i.money);
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
}
