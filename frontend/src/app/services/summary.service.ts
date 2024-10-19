import {Injectable} from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {BehaviorSubject, Observable} from 'rxjs';
import {tap} from 'rxjs/operators';
import {Summary} from '../models/summary.model';
import {Asset} from '../models/asset.model';
import {AuthService} from "../auth/auth.service";
import {SearchCriteria} from "../models/search-criteria.model";


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
    return this.http.post<Summary>(`${this.basePath}/summaries/${summary.id}/update`, summary);
  }

  updateAsset(summaryId: string, assetId: string, asset: Asset): Observable<Asset> {
    let userId = JSON.parse(localStorage.getItem("userData")).id;
    return this.http.post<Asset>(`${this.basePath}/${userId}/summaries/${summaryId}/asset/${assetId}`, asset);
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

  cancelSummary(summaryId: string) {
    let userId = JSON.parse(localStorage.getItem("userData")).id;
    return this.http.post<Summary>(`${this.basePath}/${userId}/summaries/${summaryId}/cancel`, "")
  }

  querySummaries(criteria: SearchCriteria, page: number, size: number) {
    return this.http.post<any>(`${this.basePath}/summaries`, criteria, {
      params: {
        page: page.toString(),
        size: size.toString()
      }
    });
  }

  addAsset(asset: Asset) {
    let userId = JSON.parse(localStorage.getItem("userData")).id;
    return this.http.post<Asset>(`${this.basePath}/${userId}/summaries/${asset.summaryId}/asset/add`, asset)
  }

  // updateAsset(asset: Asset) {
  //   let userId = JSON.parse(localStorage.getItem("userData")).id;
  //   return this.http.post<Asset>(`${this.basePath}/${userId}/summaries/${asset.summaryId}/asset/add`, asset)
  // }

}
