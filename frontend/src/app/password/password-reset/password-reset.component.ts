import { Component, OnInit } from '@angular/core';
import {AuthService} from "../../auth/auth.service";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-password-reset',
  templateUrl: './password-reset.component.html',
  styleUrls: ['./password-reset.component.css']
})
export class PasswordResetComponent implements OnInit {

  newPassword: string;
  token: string;
  error: string | null = null;

  constructor(
      private authService: AuthService,
      private route: ActivatedRoute,
      private router: Router
  ) {}

  ngOnInit(): void {
    this.token = this.route.snapshot.queryParamMap.get('token');
  }

  resetPassword(): void {
    this.authService.resetPassword(this.token, this.newPassword).subscribe(data => {
      console.log(data);
      this.router.navigate(["/auth"])
    }, error => {
      console.error(error);
      this.error = error.error.message
    });
  }

}
