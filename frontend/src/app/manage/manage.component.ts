import {Component, OnInit} from '@angular/core';
import {AssetService} from '../services/asset.service';
import {Asset} from '../models/asset.model';

@Component({
  selector: 'app-manage',
  templateUrl: './manage.component.html',
  styleUrls: ['./manage.component.css']
})
export class ManageComponent implements OnInit {

  assets: Asset[] = [];
  percentages: [{ [key: number]: number }] = [{}];
  sum: number;
  numberOfItems: number;

  constructor(private assetService: AssetService) {
  }

  ngOnInit(): void {
    let sumItems = 0;
    this.assetService.getAssets().subscribe(data => {
      this.assets = data;
      this.assets.forEach(asset => sumItems += asset.items.length);
      this.numberOfItems = sumItems;
    });

    this.assetService.getPercentages().subscribe(data => {
      this.percentages = data;
    });

    this.assetService.getSum().subscribe(data => {
      this.sum = data;
    });

  }


}
