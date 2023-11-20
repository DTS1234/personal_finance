import {Component, Input, OnInit} from '@angular/core';
import {Asset} from '../../models/asset.model';

@Component({
  selector: 'app-asset-box',
  templateUrl: './asset-box.component.html',
  styleUrls: ['./asset-box.component.css']
})
export class AssetBoxComponent implements OnInit {

  @Input() asset: Asset;

  constructor() {
  }

  ngOnInit(): void {
  }

}
