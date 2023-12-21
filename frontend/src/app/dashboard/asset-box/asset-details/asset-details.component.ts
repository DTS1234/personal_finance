import {Component, Inject, Input} from '@angular/core';
import {
  MAT_DIALOG_DATA,
  MatDialogActions,
  MatDialogContent,
  MatDialogRef,
  MatDialogTitle
} from "@angular/material/dialog";
import {Asset} from "../../../models/asset.model";
import {NgForOf} from "@angular/common";

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
export class AssetDetailsComponent {


  constructor(@Inject(MAT_DIALOG_DATA) public asset: Asset, public dialogRef: MatDialogRef<AssetDetailsComponent>) {
    console.log("asset details: " + JSON.stringify(asset))
  }

  public onClose() {
    this.dialogRef.close()
  }


}
