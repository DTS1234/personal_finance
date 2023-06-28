import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Asset} from '../models/asset.model';
import {Observable} from 'rxjs';
import {Summary} from '../models/summary.model';

@Injectable({
  providedIn: 'root'
})
export class AssetService {

  private basePath = 'http://localhost:8080';

  constructor(private http: HttpClient) {
  }

  getAssets(): Observable<Asset[]> {
    return this.http.get<Asset[]>(`${this.basePath}/assets`);
  }

  getPercentages(): Observable<[{ [key: number]: number }]> {
    return this.http.get<[{ [key: number]: number }]>(`${this.basePath}/assets/percentages`);
  }

  getSum(): Observable<number> {
    return this.http.get<number>(`${this.basePath}/assets/sum`);
  }

}
