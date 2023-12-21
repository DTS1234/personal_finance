import {Component, Input, OnInit} from '@angular/core';
import {Asset} from '../../models/asset.model';
import {MatDialog} from "@angular/material/dialog";
import {AssetDetailsComponent} from "./asset-details/asset-details.component";

@Component({
  selector: 'app-asset-box',
  templateUrl: './asset-box.component.html',
  styleUrls: ['./asset-box.component.css']
})
export class AssetBoxComponent implements OnInit {

  @Input() asset: Asset;

  constructor(public dialog: MatDialog) {
  }

  ngOnInit(): void {
  }

  onClick() {
    this.dialog.open(AssetDetailsComponent, {data: this.asset})
  }
}
