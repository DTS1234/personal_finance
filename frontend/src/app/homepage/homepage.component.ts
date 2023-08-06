import {Component, OnInit} from '@angular/core';
import {Asset} from '../models/asset.model';
import {AssetService} from '../services/asset.service';
import {Summary} from '../models/summary.model';
import {SummaryService} from '../services/summary.service';
import {ActivatedRoute, NavigationEnd, Router} from '@angular/router';

@Component({
  selector: 'app-homepage',
  templateUrl: './homepage.component.html',
  styleUrls: ['./homepage.component.css']
})
export class HomepageComponent implements OnInit {

  constructor(private assetService: AssetService, private summaryService: SummaryService, private router: Router,
              private activatedRoute: ActivatedRoute) {
  }

  assets: Asset[] = [];
  summaries: Summary[] = [];

  ngOnInit(): void {

    this.router.events.subscribe(event => {
      this.activatedRoute.queryParams.subscribe(params => {
        if (params.reload) {
          console.log('RELOADING HOMEPAGER AFTER receving RELOAD PARAMETER!!!');
          this.loadData();
        }
      });
    });

    console.log('RELOADING HOMEPAGE ON INIT!');
    this.loadData();

  }

  loadData(): void {

    this.summaryService.getSummaries().subscribe(summariues => {
      if (summariues == null) {
        this.summaryService.fetchSummaries().subscribe(data => {
          this.summaries = data.slice(data.length - 3);
          this.assets = this.summaries[this.summaries.length - 1].assets;
        });
      } else {
        this.summaries = summariues.slice(summariues.length - 3);
        this.assets = this.summaries[this.summaries.length - 1].assets;
      }
    });
  }
}
