import {Component} from '@angular/core';
import {FormsModule, NgForm} from "@angular/forms";
import {AuthService} from "../auth.service";
import {UserSignUpData} from "./user-signup.model";
import {User} from "../user.model";

@Component({
  selector: 'app-sign-up',
  standalone: true,
  imports: [
    FormsModule
  ],
  templateUrl: './sign-up.component.html',
  styleUrl: './sign-up.component.css'
})
export class SignUpComponent {

  error: string | null = null
  constructor(private auth: AuthService) {
  }

  onSubmit(form: NgForm) {

    if (!form.valid) {
      return
    }

    const username = form.value.email
    const password = form.value.password
    const firstname = form.value.firstname
    const lastname = form.value.lastname
    const birthdate= form.value.birthdate
    const gender = form.value.gender

    this.auth.signUp(new UserSignUpData(username, password, birthdate, gender, lastname, firstname)).subscribe(
      resData => {
        let userInformation = resData.userInformation;
        this.auth.user.next(new User(userInformation.email, "", "", new Date(),
          userInformation.firstname, userInformation.lastname, userInformation.birthdate, userInformation.gender))
        alert('A registration confirmation link has been sent to your email. Click it to activate your account.');
        }, error => {
        this.error = error.error
      }
    );
  }
}
