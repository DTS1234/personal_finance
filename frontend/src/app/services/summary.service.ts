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
    }));

  }

  createNewSummary(summary: Summary): Observable<Summary> {
    return this.http.post<Summary>('http://localhost:8080/summaries/new', summary);
  }

  updateSummary(summary: Summary): Observable<Summary> {
    return this.http.post<Summary>(`${this.basePath}/summaries/${summary.id}/update`, summary);
  }

  updateAsset(summaryId: number, assetId: number, asset: Asset): Observable<Summary> {
    return this.http.post<Summary>(`${this.basePath}/summaries/${summaryId}/updateAsset/${assetId}`, asset);
  }

  confirmSummary(summary: Summary): Observable<Summary> {
    return this.http.post<Summary>(`${this.basePath}/summaries/${summary.id}/confirm`, summary);
  }

  getSummary(id: number): Observable<Summary> {
    return this.http.get<Summary>(`${this.basePath}/summaries/${id}`);
  }

  getCurrentSummary(): Observable<Summary> {
    return this.http.get<Summary>(`${this.basePath}/summaries/current`);
  }

}
