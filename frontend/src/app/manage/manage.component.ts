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

    this.summaryService.getCurrentSummary().subscribe(s => {
        this.assets = s.assets;
        this.assets.forEach(asset => sumItems += asset.items.length);
        this.numberOfItems = sumItems;
      }
    );

    this.assetService.getPercentages().subscribe(data => {
      this.percentages = data;
    });

    this.assetService.getSum().subscribe(data => {
      this.sum = data;
    });

  }

  createNewSummary(): void {

    const dateTime = new Date(); // Current date and time
    const formattedDateTime = dateTime.toISOString().slice(0, 19);

    let newSummary: Summary = new Summary(null, formattedDateTime, 0, []);
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
