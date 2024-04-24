import {Component, OnInit} from '@angular/core';
import {loadStripe, Token} from '@stripe/stripe-js';
import {PaymentService} from "./payment.service";
import {log} from "@angular-devkit/build-angular/src/builders/ssr-dev-server";
import {Router} from "@angular/router";
import {SpinnerComponent} from "../../common/spinner/spinner.component";
import {NgIf} from "@angular/common";

@Component({
  selector: 'app-payment',
  standalone: true,
  imports: [
    SpinnerComponent,
    NgIf
  ],
  templateUrl: './payment.component.html',
  styleUrl: './payment.component.css'
})
export class PaymentComponent implements OnInit {
  protected isLoading:boolean = false;
  stripe;
  card;
  payment;

  constructor(private paymentService: PaymentService, private router: Router) {
  }

  async ngOnInit() {
    this.stripe = await loadStripe('pk_test_51O5VVwHb4TtyICkwV8q00bVbskRZj6ZsOkQzzSRdA2UpCQfGvdAjsr04EuTKwIUX7HXWpdHYGI1aSIhSNGICHxRq00bSMMX3pH');
    const elements = this.stripe.elements();
    this.card = elements.create('card');
    this.card.mount('#card-element');
  }

  async handlePayment() {
    const {token, error} = await this.stripe.createToken(this.card);
    if (error) {
      console.error(error);
    } else {
      this.submitTokenToBackend(token);
    }
  }

  submitTokenToBackend(token: Token) {
    this.isLoading = true;
    this.paymentService.submitPaymentMethod(token.id).subscribe(it => {
      this.goToSubscriptionPage()
      this.isLoading = false;
    })
  }

  goToSubscriptionPage() {
    this.router.navigate(["/subscription/buy"])
  }
}
