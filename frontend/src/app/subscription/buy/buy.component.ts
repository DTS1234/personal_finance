import {Component} from '@angular/core';
import {PaymentService} from "../payment/payment.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-buy',
  templateUrl: './buy.component.html',
  styleUrl: './buy.component.css'
})
export class BuyComponent {

  isLoading: boolean = false;

  constructor(private paymentService: PaymentService, private router: Router) {
  }

  purchaseSubscription() {
    this.isLoading = true;  // Start loading
    this.paymentService.createSubscription()
      .subscribe(data => {
        console.log(data)
        this.isLoading = false;
        this.router.navigate(['/subscription/success']);
      })
  }
}
