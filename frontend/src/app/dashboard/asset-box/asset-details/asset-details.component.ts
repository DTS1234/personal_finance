import {Component, Inject, Input, OnInit} from '@angular/core';
import {
  MAT_DIALOG_DATA,
  MatDialogActions,
  MatDialogContent,
  MatDialogRef,
  MatDialogTitle
} from "@angular/material/dialog";
import {Asset} from "../../../models/asset.model";
import {NgForOf} from "@angular/common";
import {CurrencyService} from "../../../services/currency.service";

@Component({
  selector: 'app-asset-details',
  standalone: true,
  imports: [
    MatDialogContent,
    MatDialogActions,
    NgForOf,
    MatDialogTitle
  ],
  templateUrl: './asset-details.component.html',
  styleUrl: './asset-details.component.css'
})
export class AssetDetailsComponent implements OnInit {

  currency = "EUR"

  constructor(@Inject(MAT_DIALOG_DATA) public asset: Asset,
              public dialogRef: MatDialogRef<AssetDetailsComponent>,
              public currencyService: CurrencyService) {
  }

  public onClose() {
    this.dialogRef.close()
  }

  ngOnInit(): void {
    this.currencyService.getCurrency().subscribe(data => this.currency = data)
  }

}
