import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-homepage-chart',
  templateUrl: './homepage-chart.component.html',
  styleUrls: ['./homepage-chart.component.css']
})
export class HomepageChartComponent implements OnInit {

  chartType = 'line';

  chartDatasets = [
    {data: [65, 59, 80, 81, 67, 93, 100], label: 'My First dataset'}
  ];

  chartLabels = ['January', 'February', 'March', 'April', 'May', 'June', 'July'];

  chartColors = [
    {
      backgroundColor: 'rgba(105, 0, 132, .2)',
      borderColor: 'rgba(200, 99, 132, .7)',
      borderWidth: 2,
    }
  ];

  chartOptions: any = {
    responsive: true
  };

  ngOnInit(): void {

  }

  chartClicked(event: any): void {
    console.log(event);
  }

  chartHovered(event: any): void {
    console.log(event);
  }

}
