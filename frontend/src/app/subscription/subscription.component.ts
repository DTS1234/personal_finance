import {Component} from '@angular/core';
import {PaymentService} from "../payment/payment.service";

@Component({
  selector: 'app-subscription',
  standalone: true,
  imports: [],
  templateUrl: './subscription.component.html',
  styleUrl: './subscription.component.css'
})
export class SubscriptionComponent {

  constructor(private paymentService: PaymentService) {
  }

  purchaseSubscription() {
    this.paymentService.createSubscription()
      .subscribe(data => console.log(data));
  }
}
