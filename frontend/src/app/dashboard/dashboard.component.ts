import {Component, OnInit} from '@angular/core';
import {SummaryService} from "../services/summary.service";
import {ActivatedRoute, Router} from "@angular/router";
import {Asset} from "../models/asset.model";
import {Summary} from "../models/summary.model";
import {CurrencyService} from "../services/currency.service";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  constructor(private summaryService: SummaryService, private router: Router,
              private activatedRoute: ActivatedRoute, private currencyService: CurrencyService) {
  }

  assets: Asset[] = [];
  summaries: Summary[] = [];
  currency = "EUR"
  rate: number = 1.0

  ngOnInit(): void {

    this.currencyService.getCurrency().subscribe(data => {
      this.currency = data
      this.loadData()
    })

    this.router.events.subscribe(event => {
      this.activatedRoute.queryParams.subscribe(params => {
        if (params.reload) {
          this.loadData();
        }
      });
    });

  }

  loadData(): void {
    this.summaryService.fetchSummaries().subscribe(data => {
      this.summaries = data
      this.assets = this.summaries[0].assets
      this.currencyService.getRate(this.summaries[0]).subscribe(data => this.rate = data)
    });
  }
}
