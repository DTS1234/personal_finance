import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, Router, RouterStateSnapshot, UrlTree} from "@angular/router";
import {Observable} from "rxjs";
import {AuthService} from "../auth/auth.service";
import {map, take} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class SubscriptionGuardService {

  constructor(private authService: AuthService, private router: Router) {
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    let id = this.authService.user.value.id;
    return this.authService.getSubscription(id).pipe(take(1), map(subscription => {
        this.authService.subscription.next(subscription)
        if (subscription != null) {
          return true
        } else {
          return this.router.createUrlTree(["/get-subscription"])
        }
      }
    ));
  }
}
