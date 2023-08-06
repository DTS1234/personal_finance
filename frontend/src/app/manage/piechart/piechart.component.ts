import {Component, OnInit} from '@angular/core';
import {SummaryService} from "../../services/summary.service";

@Component({
  selector: 'app-piechart',
  templateUrl: './piechart.component.html',
  styleUrls: ['./piechart.component.css']
})
export class PiechartComponent implements OnInit {

  constructor(private summaryService: SummaryService) {
  }

  chartType = 'pie';

  chartDatasets = [
    {data: [300, 50, 100, 40, 120], label: 'My First dataset'}
  ];

  chartLabels = ['Red', 'Green', 'Yellow', 'Grey', 'Dark Grey'];

  chartColors = [
    {
      backgroundColor: ['#F7464A', '#46BFBD', '#FDB45C', '#949FB1', '#4D5360'],
      hoverBackgroundColor: ['#FF5A5E', '#5AD3D1', '#FFC870', '#A8B3C5', '#616774'],
      borderWidth: 2,
    }
  ];

  chartOptions: any = {
    responsive: true,
    legend: {
      labels: {
        fontColor: 'black',
        fontSize: 18
      }
    }
  };

  chartClicked(event: any): void {
  }

  chartHovered(event: any): void {
  }

  ngOnInit(): void {
    this.summaryService.getCurrentSummary().subscribe(
      s => {
        const labels = s.assets.map(a => a.name);
        const sums = s.assets.map(a => a.moneyValue);


        this.chartDatasets = [
          {data: sums, label: 'My First dataset'}
        ];

        this.chartLabels = labels;

      }
    );

  }

}
