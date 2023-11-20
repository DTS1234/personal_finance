import {Component, OnInit} from '@angular/core';
import {Asset} from '../../models/asset.model';
import {SummaryService} from '../../services/summary.service';
import {Summary} from '../../models/summary.model';
import {Router} from '@angular/router';

@Component({
  selector: 'app-summary-creation',
  templateUrl: './summary-creation.component.html',
  styleUrls: ['./summary-creation.component.css']
})
export class SummaryCreationComponent implements OnInit {
  availableAssets: Asset[] = [];
  newAssets: Asset[] = [];
  activeAsset: Asset = new Asset(null, 'No assets yet', 0, []);
  summary: Summary;

  constructor(private summaryService: SummaryService,
              private router: Router) {
  }

  ngOnInit(): void {

    this.summaryService.newSummary$.subscribe(
      newSum => {
        this.summary = newSum;
        this.availableAssets = this.summary?.assets;
        console.log("NEW summary loaded: " + this.summary.money)
      }
    );

  }

  addAssetPage(): void {
    const id = this.summary?.id == null ? this.router.url.substring(this.router.url.length - 2)
      : this.summary.id;
    this.router.navigate([`/summary/${id}/add-asset`]).then(r => console.log(r));
  }

  confirmSummary(): void {
    console.log("confirming summary: " + JSON.stringify(this.summary))
    this.summaryService.confirmSummary(this.summary).subscribe(
      data => {
        console.log("summary confirmed: " + JSON.stringify(data))
        this.summaryService.setNewSummary(null)
        this.router.navigate([`/dashboard`]).then(r => console.log(r));
      }
    )
  }

  remove(asset: Asset): void {
    const indexToRemove = this.availableAssets.indexOf(asset);
    this.availableAssets.splice(indexToRemove, 1);
  }

  edit(asset: Asset): void {
    this.router.navigate([`/summary/${this.summary.id}/edit-asset`],
      {queryParams: {asset: JSON.stringify(asset)}}).then(r => console.log(r));
  }
}
