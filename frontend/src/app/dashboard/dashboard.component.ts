import { Component, OnInit } from '@angular/core';
import {AssetService} from "../services/asset.service";
import {SummaryService} from "../services/summary.service";
import {ActivatedRoute, Router} from "@angular/router";
import {Asset} from "../models/asset.model";
import {Summary} from "../models/summary.model";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  constructor(private assetService: AssetService, private summaryService: SummaryService, private router: Router,
              private activatedRoute: ActivatedRoute) {
  }

  assets: Asset[] = [];
  summaries: Summary[] = [];

  ngOnInit(): void {

    this.router.events.subscribe(event => {
      this.activatedRoute.queryParams.subscribe(params => {
        if (params.reload) {
          this.loadData();
        }
      });
    });

    this.loadData();

  }

  loadData(): void {

    this.summaryService.getSummaries().subscribe(summaries => {
      if (summaries == null) {
        this.summaryService.fetchSummaries().subscribe(data => {
          this.summaries = data.slice(data.length - 3);
          this.assets = this.summaries[this.summaries.length - 1].assets;
        });
      } else {
        this.summaries = summaries.slice(summaries.length - 3);
        this.assets = this.summaries[this.summaries.length - 1].assets;
      }
    });
  }

}
