import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Asset} from '../models/asset.model';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AssetService {

  constructor(private http: HttpClient) {
  }

  getAssets(): Observable<Asset[]> {
    return this.http.get<Asset[]>('http://localhost:8080/assets');
  }

  getPercentages(): Observable<[{ [key: number]: number }]> {
    return this.http.get<[{ [key: number]: number }]>('http://localhost:8080/assets/percentages');
  }

  getSum(): Observable<number> {
    return this.http.get<number>('http://localhost:8080/assets/sum');
  }

}
