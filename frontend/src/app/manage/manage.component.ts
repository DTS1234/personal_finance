import {Component, OnInit} from '@angular/core';
import {AssetService} from '../services/asset.service';
import {Asset} from '../models/asset.model';
import {SummaryService} from "../services/summary.service";
import {Summary} from "../models/summary.model";
import {Router} from "@angular/router";

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

  constructor(private assetService: AssetService, private summaryService: SummaryService, private router: Router) {
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

  createNewSummary(): void {
    const date = new Date(Date.now());
    console.log(date);
    let newSummary: Summary = new Summary(null, '06/27/2022', 0, []);
    this.summaryService.createNewSummary(newSummary).subscribe(
      data => {
        newSummary = data;
        console.log(newSummary);
        this.summaryService.setNewSummary(newSummary);
        this.router.navigate([`/summary/${newSummary.id}/`]).then(r => console.log(r));

      }
    );
  }
}
