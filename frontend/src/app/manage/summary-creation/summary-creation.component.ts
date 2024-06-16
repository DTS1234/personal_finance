import {Component, OnInit} from '@angular/core';
import {Asset} from '../../models/asset.model';
import {SummaryService} from '../../services/summary.service';
import {Summary} from '../../models/summary.model';
import {Router} from '@angular/router';
import {CurrencyService} from "../../services/currency.service";

@Component({
  selector: 'app-summary-creation',
  templateUrl: './summary-creation.component.html',
  styleUrls: ['./summary-creation.component.css']
})
export class SummaryCreationComponent implements OnInit {
  availableAssets: Asset[] = [];
  newAssets: Asset[] = [];
  summary: Summary;

  currency = "EUR"
  rate: number = 1.00 // rate applied on the ui values

  constructor(private summaryService: SummaryService,
              private router: Router,
              private currencyService: CurrencyService) {
  }

  ngOnInit(): void {
    this.currencyService.getCurrency().subscribe(currentCurrency => {
      this.currency = currentCurrency
      this.fetchCurrentDraftSummary();
    })
  }

  private fetchCurrentDraftSummary() {
    this.summaryService.getCurrentDraft().subscribe(data => {
      this.summary = data
      this.availableAssets = this.summary?.assets;
      this.currencyService.getRate(data).subscribe(
        data => this.rate = data
      )
    })
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

  edit(index: number): void {
    this.router.navigate([`/summary/${this.summary.id}/add-asset`],
      {queryParams: {index: JSON.stringify(index)}}).then(r => console.log(r));
  }

  cancelSummary() {
    this.summaryService.cancelSummary(this.summary.id).subscribe(
      data => {
        console.log("summary cancelled: " + data.id)
        this.summaryService.setNewSummary(null)
        this.router.navigate(["/dashboard"])
      }
    )
  }

  removeAsset(index: number) {
    const newSummary = JSON.parse(JSON.stringify(this.summary));
    newSummary.assets.splice(index, 1)
    newSummary.money -= this.summary.assets[index].money

    this.summaryService.updateSummary(newSummary)
      .subscribe(data => {
        this.summary = data;
        this.summaryService.setNewSummary(data);
        this.availableAssets.splice(index, 1);
      })
  }
}
