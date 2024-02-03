import {Component, OnInit} from '@angular/core';
import {Asset} from '../models/asset.model';
import {SummaryService} from "../services/summary.service";
import {Summary} from "../models/summary.model";
import {Router} from "@angular/router";
import {CurrencyService} from "../services/currency.service";

@Component({
  selector: 'app-manage',
  templateUrl: './manage.component.html',
  styleUrls: ['./manage.component.css']
})
export class ManageComponent implements OnInit {
  assets: Asset[] = [];
  sum: number = 0;
  numberOfItems: number;
  percentages = []
  inSummaryCreation: boolean = false;

  // currency management variables
  currency = "EUR"
  rate: number = 1.00 // rate applied on the ui values

  constructor(private summaryService: SummaryService, private currencyService: CurrencyService, private router: Router) {
  }

  ngOnInit(): void {

    this.currencyService.getCurrency().subscribe(currentCurrency => {
      this.currency = currentCurrency;
      this.fetchSummaries()
    })

    this.summaryService.newSummary$.subscribe(data => {
      if (data != null) {
        this.inSummaryCreation = true;
      } else {
        this.summaryService.getCurrentDraft().subscribe(
          data => {
            if (data != null) {
              this.summaryService.setNewSummary(data)

              this.currencyService.getRate(data).subscribe(data => {
                this.rate = data;
              })

              this.inSummaryCreation = true;
            } else {
              this.inSummaryCreation = false;
            }
          }
        )
      }
    })
  }

  private fetchSummaries() {
    this.summaryService.fetchSummaries().subscribe(data => {
      if (data[0] != null) {
        this.assets = data[0].assets
        this.numberOfItems = this.assets.map(a => a.items.length).reduce((a, b) => a + b, 0)
        this.sum = this.assets.map(a => a.money).reduce((a, b) => a + b, 0) as number;
        this.percentages = this.assets.map(a => a.money / this.sum)

        this.currencyService.getRate(data[0]).subscribe(data => {
          this.rate = data
        })
      }
    })
  }

  createNewSummary(): void {
    const dateTime = new Date(); // Current date and time
    const formattedDateTime = dateTime.toISOString().slice(0, 19);

    let newSummary: Summary = new Summary(null, formattedDateTime, 0, this.currency, []);
    this.summaryService.createNewSummary(newSummary)
      .subscribe(
        data => {
          newSummary = data;
          this.summaryService.setNewSummary(newSummary);
          this.router.navigate([`/summary/${newSummary.id}/`]).then(r => console.log(r));
        }
      );
  }

  continueCreation(): void {
    this.summaryService.getCurrentDraft().subscribe(
      data => {
        this.router.navigate([`/summary/${data.id}/`]).then(r => console.log(r));
      }
    )
  }
}
