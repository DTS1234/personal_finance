import {Component, Input, OnInit} from '@angular/core';
import {Asset} from '../../models/asset.model';
import {AssetService} from '../../services/asset.service';
import {SummaryService} from '../../services/summary.service';
import {Summary} from '../../models/summary.model';
import {ActivatedRoute, NavigationExtras, Router} from '@angular/router';
import {AssetSharedService} from '../../services/add-asset.service';

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

  constructor(private assetService: AssetService,
              private summaryService: SummaryService,
              private router: Router,
              private activatedRoute: ActivatedRoute,
              private addAssetService: AssetSharedService) {
  }

  ngOnInit(): void {

    // this.router.events.subscribe(event => {
    //   this.activatedRoute.queryParams.subscribe(params => {
    //     if (params.reload) {// Perform data reloading or any other actions here
    //       console.log('dupa');
    //     }
    //   });
    // })};

    this.router.events.subscribe(event => {
      this.activatedRoute.queryParams.subscribe(params => {
        if (params.reload) {
          this.loadData();
        }
      });
    });

    this.summaryService.onNewSummaryCreation().subscribe(
      newSum => {
        this.summary = newSum;
        this.availableAssets = this.summary?.assets;
      }
    );

    this.loadData();

  }

  private loadData(): void {
    console.log(this.activatedRoute.params.subscribe(p => {

      this.summaryService.getSummary(Number(p.id)).subscribe(s => {
        this.summary = s;
        this.availableAssets = s.assets;
      });

    }));

    this.addAssetService.availableAssets$.subscribe((assets) => {
      this.newAssets = assets;
    });
  }

  addAssetPage(): void {
    const id = this.summary?.id == null ? this.router.url.substring(this.router.url.length - 2) : this.summary.id;
    this.router.navigate([`/summary/${id}/add-asset`]).then(r => console.log(r));
  }

  confirmSummary(): void {

    if (this.newAssets != null && this.newAssets.length > 0) {
      this.summary.assets = this.availableAssets.concat(this.newAssets);
    } else {
      this.summary.assets = this.availableAssets;
    }

    this.summaryService.updateSummary(this.summary).subscribe(s => {
        console.log(s);
        this.summaryService.confirmSummary(s).subscribe(confirmedSummary => console.log('summary confirmed : ' + confirmedSummary));
      }
    );
    this.addAssetService.clearAssets();

    const navigationExtras: NavigationExtras = {
      queryParams: {reload: true} // Add a query parameter to force reload
    };

    this.router.navigate(['homepage'], navigationExtras).then(r => console.log(r));
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
