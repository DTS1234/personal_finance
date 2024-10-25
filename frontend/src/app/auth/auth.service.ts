import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {BehaviorSubject, Observable, of} from "rxjs";
import {User} from "./user.model";
import {catchError, tap} from "rxjs/operators";
import {Router} from "@angular/router";
import {UserSignUpData} from "./sign-up/user-signup.model";
import {Subscription} from "../account/subscription.model";
import {UserInformationData} from "./user-updata.model";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  authenticated = false;
  user = new BehaviorSubject<User>(null)
  subscription: BehaviorSubject<Subscription> = new BehaviorSubject<Subscription>(null)
  token: string | null = null
  private tokenExpirationTimer: any;

  constructor(private http: HttpClient, private router: Router) {
  }

  private baseUrl = environment.useInMemoryDb ? 'http://localhost:3000' : 'http://localhost:8080';

  login(username: string, password: string): Observable<AuthResponseData> {
    return this.http.post<AuthResponseData>(`${this.baseUrl}/login`, {username, password})
      .pipe(tap(resData => {
        this.handleAuthentication(resData);
      }));
  }

  signUp(user: UserSignUpData): Observable<SignUpResponseData> {
    return this.http.post<SignUpResponseData>(`${this.baseUrl}/registration`, user);
  }

  getSubscription(userId: string): Observable<Subscription> {
    return this.http.get<Subscription>(this.baseUrl + `/${userId}/subscription`)
      .pipe(
        catchError(err => {
          console.error('No subscription for user.', err);
          return of(null); // Return null if there's an error or no subscription
        }));
  }

  private handleAuthentication(resData: AuthResponseData) {
    const expirationDate = new Date(new Date().getTime() + +resData.expiresIn * 1000)

    let userInformation = resData.userInformation;
    const user = new User(resData.username, resData.id, resData.token, expirationDate,
      userInformation.firstname, userInformation.lastname,
      userInformation.birthdate, userInformation.gender)

    this.user.next(user)
    this.autoLogout(+resData.expiresIn * 1000)
    localStorage.setItem("userData", JSON.stringify(user))
  }

  autoLogin() {
    const userData = JSON.parse(localStorage.getItem("userData"));
    if (!userData) {
      return;
    }
    const loadedUser = new User(userData.username, userData.id, userData._token, new Date(userData._tokenExpirationDate),
      userData.firstname,
      userData.lastname,
      userData.birthdate,
      userData.gender)
    if (loadedUser.token) {
      this.user.next(loadedUser);
      const expirationDuration = new Date(userData._tokenExpirationDate).getTime() - new Date().getTime()
      this.autoLogout(expirationDuration)
    }
  }

  logout(): void {
    this.user.next(null)
    this.router.navigate(["/homepage"]);
    localStorage.removeItem("userData");
    if (this.tokenExpirationTimer
    ) {
      clearTimeout(this.tokenExpirationTimer)
    }
    this.tokenExpirationTimer = null
  }

  autoLogout(expirationDuration: number) {
    this.tokenExpirationTimer = setTimeout(() => {
      this.logout();
    }, expirationDuration)
  }

  requestPasswordReset(email: string): Observable<PasswordResetConfirmation> {
    return this.http.post<PasswordResetConfirmation>(this.baseUrl + '/password_reset/request', <PasswordResetRequest>{email: email})
  }

  resetPassword(token: string, newPassword: string): Observable<PasswordResetConfirmation> {
    return this.http.post<PasswordResetConfirmation>(this.baseUrl + '/password_reset', <PasswordReset>{
      token: token,
      newPassword: newPassword
    })
  }

  updateUserInfo(userInformation: UserInformationData): Observable<UserInformationData> {
    let userData = JSON.parse(localStorage.getItem("userData"));
    const userId = userData.id
    console.log(userInformation)
    return this.http.post<UserInformationData>(this.baseUrl + `/${userId}/update`, userInformation)
  }
}

export interface PasswordResetConfirmation {
  message: string,
  state: PasswordResetState
}

enum PasswordResetState {
  IN_PROGRESS, SUCCESS, FAILED
}

interface PasswordResetRequest {
  email: string
}

interface PasswordReset {
  token: string,
  newPassword: string
}

export interface AuthResponseData {
  username: string,
  id: string,
  token: string,
  expiresIn: string,
  userInformation: UserSignUpData
}

export interface SignUpResponseData {
  username: string,
  message: string,
  userInformation: UserSignUpData
}
