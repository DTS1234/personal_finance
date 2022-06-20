import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Asset} from '../models/asset.model';
import {tap} from 'rxjs/operators';
import {Summary} from "../models/summary.model";


@Injectable({
  providedIn: 'root'
})
export class SummaryService {

  constructor(private http: HttpClient) {
  }

  getSummaries(): Observable<Summary[]> {

    return this.http.get<Summary[]>('http://localhost:8080/summaries').pipe(tap(event => {
      console.log(event);
    }));

  }

}
