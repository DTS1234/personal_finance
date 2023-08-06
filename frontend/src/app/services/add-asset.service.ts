import {Injectable} from '@angular/core';
import {BehaviorSubject} from 'rxjs';
import {Asset} from '../models/asset.model';
import {AssetService} from './asset.service';

@Injectable({
  providedIn: 'root'
})
export class AssetSharedService {

  constructor(private assetService: AssetService) {
  }

  private newAssets = new BehaviorSubject<Asset[]>([]);

  newAssets$ = this.newAssets.asObservable();

  addToNewAssets(asset: Asset): void {
    const currentAssets = this.newAssets.value;
    currentAssets.push(asset);
    this.newAssets.next(currentAssets);
  }

  clearNewAssets(): void {
    const currentAssets = this.newAssets.value;
    currentAssets.splice(0);
    this.newAssets.next(currentAssets);
  }

}
