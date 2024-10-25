import {Injectable} from "@angular/core";
import { HttpEvent, HttpHandler, HttpInterceptor, HttpParams, HttpRequest } from "@angular/common/http";
import {Observable} from "rxjs";
import {AuthService} from "./auth.service";
import {exhaustMap, take} from "rxjs/operators";

@Injectable()
export class AuthInterceptorService implements HttpInterceptor {

  constructor(private authService: AuthService) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return this.authService.user.pipe(
      take(1), exhaustMap(user => {

        if (!user) {
          console.log("user is null!")
          return next.handle(req)
        }

        console.log("token: " + user.token)

        const modifiedRequest = req.clone({
          setHeaders: { Authorization: `Bearer ${user.token}` }
        })
        return next.handle(modifiedRequest)
      })
    )
  }
}
