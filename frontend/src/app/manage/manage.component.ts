import {Component, OnInit} from '@angular/core';
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
  sum: number;
  numberOfItems: number;
  inSummaryCreation: boolean = false;

  constructor(private summaryService: SummaryService, private router: Router) {
  }

  ngOnInit(): void {
    this.summaryService.newSummary$.subscribe(data => {
      if (data != null) {
        this.inSummaryCreation = true;
      }
    })
  }

  createNewSummary(): void {
    const dateTime = new Date(); // Current date and time
    const formattedDateTime = dateTime.toISOString().slice(0, 19);

    let newSummary: Summary = new Summary(null, formattedDateTime, 0, []);
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
    this.summaryService.newSummary$
      .subscribe(
        data => {
          console.log(data)
          if(data != null) {
            this.router.navigate([`/summary/${data.id}/`]).then(r => console.log(r));
          }
        }
      );
  }

}
