import {Component, Input, OnInit} from '@angular/core';
import {Summary} from '../../models/summary.model';
import {DatePipe} from "@angular/common";

@Component({
  selector: 'app-summary-box',
  templateUrl: './summary-box.component.html',
  styleUrls: ['./summary-box.component.css']
})
export class SummaryBoxComponent implements OnInit {

  @Input() summary: Summary;

  constructor(private datePipe: DatePipe) {
  }

  ngOnInit(): void {
  }

  formatDate(date: string): string {
    return this.datePipe.transform(date, 'yyyy-MM-dd HH:mm:ss');
  }

}
