import {Component, OnInit} from '@angular/core';
import {User} from "../auth/user.model";
import {AuthService} from "../services/auth.service";

@Component({
  selector: 'app-account',
  standalone: true,
  imports: [],
  templateUrl: './account.component.html',
  styleUrl: './account.component.css'
})
export class AccountComponent {

  protected user: User;

  constructor(authService:AuthService) {
    authService.user.subscribe(user => this.user = user)
  }

}
