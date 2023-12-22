import {Component, Input, OnInit} from '@angular/core';
import {Asset} from '../../models/asset.model';
import {MatDialog} from "@angular/material/dialog";
import {AssetDetailsComponent} from "./asset-details/asset-details.component";
import {CurrencyService} from "../../services/currency.service";

@Component({
  selector: 'app-asset-box',
  templateUrl: './asset-box.component.html',
  styleUrls: ['./asset-box.component.css']
})
export class AssetBoxComponent implements OnInit {

  @Input() asset: Asset;
  currency = "EUR"

  constructor(public dialog: MatDialog, public currencyService: CurrencyService) {
  }

  ngOnInit(): void {
    this.currencyService.getCurrency().subscribe(data => this.currency = data)
  }

  onClick() {
    this.dialog.open(AssetDetailsComponent, {data: this.asset})
  }
}
