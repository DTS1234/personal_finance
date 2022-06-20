import {Component, Input, OnInit} from '@angular/core';
import {Summary} from '../../models/summary.model';

@Component({
  selector: 'app-summary-box',
  templateUrl: './summary-box.component.html',
  styleUrls: ['./summary-box.component.css']
})
export class SummaryBoxComponent implements OnInit {

  @Input() summary: Summary;

  constructor() {
  }

  ngOnInit(): void {
  }

}
