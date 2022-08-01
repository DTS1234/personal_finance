import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {BehaviorSubject, Observable} from 'rxjs';
import {tap} from 'rxjs/operators';
import {Summary} from '../models/summary.model';
import {Asset} from "../models/asset.model";


@Injectable({
  providedIn: 'root'
})
export class SummaryService {

  private newSummary: BehaviorSubject<Summary> = new BehaviorSubject<Summary>(null);
  private basePath = 'http://localhost:8080';

  constructor(private http: HttpClient) {
  }

  onNewSummaryCreation(): Observable<Summary> {
    return this.newSummary.asObservable();
  }

  setNewSummary(newSummary: Summary): void {
    this.newSummary.next(newSummary);
  }

  resetNewSummary(): void {
    this.newSummary.next(null);
  }

  getSummaries(): Observable<Summary[]> {

    return this.http.get<Summary[]>('http://localhost:8080/summaries').pipe(tap(event => {
      console.log(event);
    }));

  }

  createNewSummary(summary: Summary): Observable<Summary> {
    return this.http.post<Summary>('http://localhost:8080/summaries/new', summary);
  }

  addAsset(asset: Asset, summary: Summary): Observable<Summary> {
    summary.assets.push(asset);
    return this.http.post<Summary>(`${this.basePath}/summaries/${summary.id}/add_asset`, summary);
  }

}
