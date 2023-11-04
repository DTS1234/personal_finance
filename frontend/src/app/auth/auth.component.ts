import {Component, OnInit} from '@angular/core';
import {AuthService} from "../services/auth.service";
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";
import {NgForm} from "@angular/forms";

@Component({
  selector: 'app-login',
  templateUrl: './auth.component.html',
  styleUrls: ['./auth.component.css']
})
export class AuthComponent {

  error : string | null = null
  isLoginMode = true

  constructor(private auth: AuthService, private http: HttpClient, private router: Router) {
  }

  onSwitchMode() {
    this.isLoginMode = !this.isLoginMode
  }

  onSubmit(authForm: NgForm) {

    if (!authForm.valid) {
      return
    }

    const username = authForm.value.username
    const password = authForm.value.password

    if (this.isLoginMode) {
      this.auth.login(username, password).subscribe(
        resData => {
          console.log(resData)
          this.router.navigate(['/dashboard'])
        }, error => {
          console.log(error)
          this.error = error
        }
      );
    } else {
      this.auth.signUp(username, password).subscribe(
        resData => {
          console.log(resData)
        }, error => {
          console.log(error)
          this.error = error
        }
      );
    }

    authForm.reset()
  }

}
