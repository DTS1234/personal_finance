import {Component} from '@angular/core';
import {AuthService} from "../../services/auth.service";
import {FormsModule, NgForm} from "@angular/forms";
import {Router} from "@angular/router";

@Component({
  selector: 'app-login-component',
  standalone: true,
  imports: [
    FormsModule
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {

  error: string | null = null

  constructor(private auth: AuthService, private router: Router) {
  }

  onSubmit(authForm: NgForm) {

    if (!authForm.valid) {
      return
    }

    const username = authForm.value.username
    const password = authForm.value.password

    this.auth.login(username, password).subscribe({
      next: (resData) => {
        this.router.navigate(['/dashboard'])
      }, error: (data) => {
        this.error = data.error.message
      }
    });

    authForm.reset()
  }

}
