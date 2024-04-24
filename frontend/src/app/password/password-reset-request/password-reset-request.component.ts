import { Component, OnInit } from '@angular/core';
import {AuthService} from "../../auth/auth.service";

@Component({
  selector: 'app-password-reset-request',
  templateUrl: './password-reset-request.component.html',
  styleUrls: ['./password-reset-request.component.css']
})
export class PasswordResetRequestComponent {

  email: string;

  constructor(private authService: AuthService) {}

  requestPasswordReset(): void {
    this.authService.requestPasswordReset(this.email).subscribe(data => {
      console.log(data);
      // Inform the user that the email was sent
    }, error => {
      console.error(error);
      // Handle errors (e.g., user not found)
    });
  }

}
