import {Component, OnInit} from '@angular/core';
import {Asset} from '../../models/asset.model';
import {AssetService} from '../../services/asset.service';

@Component({
  selector: 'app-summary-creation',
  templateUrl: './summary-creation.component.html',
  styleUrls: ['./summary-creation.component.css']
})
export class SummaryCreationComponent implements OnInit {
  availableAssets: Asset[] = [];
  activeAsset: Asset;

  constructor(private assetService: AssetService) {
  }

  ngOnInit(): void {
    this.assetService.getAssets().subscribe(data => {
        console.log(data);
        this.availableAssets = data;
        this.activeAsset = this.availableAssets[0];
      }
    );


  }

}
