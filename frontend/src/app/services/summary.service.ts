import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {BehaviorSubject, Observable} from 'rxjs';
import {share, take, tap} from 'rxjs/operators';
import {Summary} from '../models/summary.model';
import {Asset} from '../models/asset.model';
import {AuthService} from "./auth.service";


@Injectable({
  providedIn: 'root'
})
export class SummaryService {

  private newSummary: BehaviorSubject<Summary> = new BehaviorSubject<Summary>(null);
  newSummary$ = this.newSummary.asObservable();

  private basePath = 'http://localhost:8080';
  private summaries: BehaviorSubject<Summary[]> = new BehaviorSubject<Summary[]>(null);

  constructor(private http: HttpClient, private authService: AuthService) {
  }

  setNewSummary(newSummary: Summary): void {
    this.newSummary.next(newSummary);
  }

  resetNewSummary(): void {
    this.newSummary.next(null);
  }

  fetchSummaries(): Observable<Summary[]> {
    let id = JSON.parse(localStorage.getItem("userData")).id;
    return this.http.get<Summary[]>(`${this.basePath}/${id}/summaries`)
      .pipe(
        tap(event => {
            console.log(event);
          }
        )
      );
  }

  setSummaries(summaries: Summary[]): void {
    this.summaries.next(summaries);
  }

  getSummaries(): Observable<Summary[]> {
    return this.summaries.asObservable();
  }

  createNewSummary(summary: Summary): Observable<Summary> {
    let userId = JSON.parse(localStorage.getItem("userData")).id;
    return this.http.post<Summary>(`${this.basePath}/${userId}/summaries/new`, summary);
  }

  updateSummary(summary: Summary): Observable<Summary> {
    let userId = JSON.parse(localStorage.getItem("userData")).id;
    return this.http.post<Summary>(`${this.basePath}/${userId}/summaries/${summary.id}/update`, summary);
  }

  updateAsset(summaryId: number, assetId: number, asset: Asset): Observable<Summary> {
    return this.http.post<Summary>(`${this.basePath}/summaries/${summaryId}/updateAsset/${assetId}`, asset);
  }

  confirmSummary(summary: Summary): Observable<Summary> {
    let userId = JSON.parse(localStorage.getItem("userData")).id;
    return this.http.post<Summary>(`${this.basePath}/${userId}/summaries/${summary.id}/confirm`, summary);
  }

  getSummary(id: number): Observable<Summary> {
    return this.http.get<Summary>(`${this.basePath}/summaries/${id}`);
  }

  getCurrentDraft() {
    let userId = JSON.parse(localStorage.getItem("userData")).id;
    return this.http.get<Summary>(`${this.basePath}/${userId}/summaries/current`)
  }

  cancelSummary(summaryId: number) {
    let userId = JSON.parse(localStorage.getItem("userData")).id;
    return this.http.post<Summary>(`${this.basePath}/${userId}/summaries/${summaryId}/cancel`, "")
  }
}
