import {Component} from '@angular/core';
import {Router} from "@angular/router";

@Component({
  selector: 'app-no-subscription',
  standalone: true,
  imports: [],
  templateUrl: './no-subscription.component.html',
  styleUrl: './no-subscription.component.css'
})
export class NoSubscriptionComponent {

  constructor(private router: Router) {
  }

  goToSubscriptionPage() {
    this.router.navigate(['/subscription/payment']);
  }

}
