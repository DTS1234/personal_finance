import {Injectable} from '@angular/core';
import { ActivatedRouteSnapshot, Router, RouterStateSnapshot, UrlTree } from "@angular/router";
import {Observable} from "rxjs";
import {AuthService} from "../auth/auth.service";
import {map, take, tap} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class AuthGuardService {

  constructor(private authService: AuthService, private router: Router) {
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    return this.authService.user.pipe(take(1), map(user => {
      let isAuth = !!user;
      if (isAuth) {
        return true;
      } else {
        return this.router.createUrlTree(["/import {Injectable} from '@angular/core';\n" +
        "import { ActivatedRouteSnapshot, Router, RouterStateSnapshot, UrlTree } from \"@angular/router\";\n" +
        "import {Observable} from \"rxjs\";\n" +
        "import {AuthService} from \"../auth/auth.service\";\n" +
        "import {map, take, tap} from \"rxjs/operators\";\n" +
        "\n" +
        "@Injectable({\n" +
        "  providedIn: 'root'\n" +
        "})\n" +
        "export class AuthGuardService {\n" +
        "\n" +
        "  constructor(private authService: AuthService, private router: Router) {\n" +
        "  }\n" +
        "\n" +
        "  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {\n" +
        "    return this.authService.user.pipe(take(1), map(user => {\n" +
        "      let isAuth = !!user;\n" +
        "      if (isAuth) {\n" +
        "        return true;\n" +
        "      } else {\n" +
        "        return this.router.createUrlTree([\"/auth\"])\n" +
        "      }\n" +
        "    }));\n" +
        "  }\n" +
        "}\nauth"])
      }
    }));
  }
}
