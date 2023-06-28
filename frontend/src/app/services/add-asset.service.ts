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

  private availableAssetsSubject = new BehaviorSubject<Asset[]>([]);

  availableAssets$ = this.availableAssetsSubject.asObservable();

  addAsset(asset: Asset): void {
    const currentAssets = this.availableAssetsSubject.value;
    currentAssets.push(asset);
    this.availableAssetsSubject.next(currentAssets);
  }

  clearAssets(): void {
    const currentAssets = this.availableAssetsSubject.value;
    currentAssets.splice(0);
    this.availableAssetsSubject.next(currentAssets);
  }

}
