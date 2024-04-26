import {Component} from '@angular/core';
import {User} from "../auth/user.model";
import {AuthService} from "../auth/auth.service";
import {Subscription} from "./subscription.model";
import {NgIf} from "@angular/common";
import {Router} from "@angular/router";
import {FormsModule} from "@angular/forms";
import {UserInformationData} from "../auth/user-updata.model";

@Component({
  selector: 'app-account',
  standalone: true,
  imports: [
    NgIf,
    FormsModule
  ],
  templateUrl: './account.component.html',
  styleUrl: './account.component.css'
})
export class AccountComponent {

  protected userModel: User;
  protected subscription: Subscription | null = null;
  protected isEditMode = false;

  userForm = {
    firstname: '',
    lastname: '',
    birthdate: '',
    gender: ''
  };

  constructor(private authService: AuthService, public router: Router) {
    authService.user.subscribe(user => {
        console.log("user loaded: " + JSON.stringify(user))
        this.userModel = user
      }
    )
    authService.getSubscription(this.userModel.id).subscribe(it => {
      this.subscription = it
      console.log(this.subscription)
    });
  }

  goToPayment() {
    this.router.navigate(["/subscription/payment"])
  }

  toggleEditMode() {
    this.isEditMode = !this.isEditMode;
  }

  updateUserInfo() {
    this.authService.updateUserInfo(new UserInformationData(this.userModel.username, new Date(this.userModel.birthdate), this.userModel.gender, this.userModel.lastname, this.userModel.firstname))
      .subscribe(userData => {
        this.authService.user.next(this.userModel)
        this.toggleEditMode()
      })
  }
}
