import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {BehaviorSubject, Observable, throwError} from "rxjs";
import {catchError, tap} from "rxjs/operators";
import {User} from "../auth/user.model";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  authenticated = false;
  user = new BehaviorSubject<User>(null)
  token: string | null = null

  constructor(private http: HttpClient) {
  }

  private baseUrl = 'http://localhost:8080';

  login(username: string, password: string): Observable<AuthResponseData> {
    return this.http.post<AuthResponseData>(this.baseUrl + '/login', {username: username, password: password})
      .pipe(
        catchError(errorRes => this.handleError(errorRes)),
        tap(resData => {
          this.handleAuthentication(resData)
        })
      );
  }

  signUp(username: string, password: string): Observable<AuthResponseData> {
    return this.http.post<AuthResponseData>(this.baseUrl + '/signup', {username: username, password: password})
      .pipe(
        catchError(errorRes => this.handleError(errorRes)),
        tap(resData => {
          this.handleAuthentication(resData);
        })
      );
  }

  private handleAuthentication(resData: AuthResponseData) {
    const expirationDate = new Date(new Date().getTime() + +resData.expiresIn * 1000)
    const user = new User(resData.username, resData.id, resData.token, expirationDate)
    this.user.next(user)
  }

  logout(): void {
    this.user.next(null)
  }

  private handleError(errorRes: HttpErrorResponse) {
    let errorMsg = "An unknown error message"
    console.log(errorRes)
    if (!errorRes.error || !errorRes.error.error) {
      return throwError(errorMsg)
    }
    switch (errorRes.error.error.message) {
      case 'LOGIN FAILED':
        errorMsg = "Login failed make sure you have passed correct credentials"
      case 'SIGN UP FAILED':
        errorMsg = "User with that username already exists"
    }
    return throwError(errorMsg)
  }

}

interface AuthResponseData {
  username: string,
  expiresIn: string,
  id: string,
  token: string
}
