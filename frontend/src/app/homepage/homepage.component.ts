import {Component, OnInit} from '@angular/core';
import {Asset} from '../models/asset.model';
import {AssetService} from '../services/asset.service';
import {Summary} from '../models/summary.model';
import {SummaryService} from '../services/summary.service';

@Component({
  selector: 'app-homepage',
  templateUrl: './homepage.component.html',
  styleUrls: ['./homepage.component.css']
})
export class HomepageComponent implements OnInit {

  constructor(private assetService: AssetService, private summaryService: SummaryService) {
  }

  assets: Asset[] = [];
  summaries: Summary[] = [];

  ngOnInit(): void {
    this.assetService.getAssets().subscribe(data => {
      console.log(data);
      this.assets = data;
    });

    this.summaryService.getSummaries().subscribe(data => {
      console.log(data);
      this.summaries = data;
    });


  }

}
