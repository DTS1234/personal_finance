import {Component, Input, OnInit} from '@angular/core';
import {Asset} from '../../models/asset.model';
import {AssetService} from '../../services/asset.service';
import {SummaryService} from '../../services/summary.service';
import {Summary} from '../../models/summary.model';
import {Router} from "@angular/router";

@Component({
  selector: 'app-summary-creation',
  templateUrl: './summary-creation.component.html',
  styleUrls: ['./summary-creation.component.css']
})
export class SummaryCreationComponent implements OnInit {
  availableAssets: Asset[] = [];
  activeAsset: Asset = new Asset(null, 'No assets yet', 0, []);
  summary: Summary;

  constructor(private assetService: AssetService, private summaryService: SummaryService, private router: Router) {
  }

  ngOnInit(): void {

    this.summaryService.onNewSummaryCreation().subscribe(
      newSum => {
        this.summary = newSum;
        this.availableAssets = [];
      }
    );

  }

}
