import {Component, OnInit} from '@angular/core';
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

  constructor(private summaryService: SummaryService, private router: Router,
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
    this.summaryService.fetchSummaries().subscribe(data => {
      this.summaries = data
      this.assets = this.summaries[0].assets
      console.log(this.assets)
    })
  }

}
