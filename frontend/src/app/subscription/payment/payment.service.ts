import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {tap} from 'rxjs/operators';
import {SubscriptionRequest} from "../../models/subscription-request.model";
import {PaymentMethodSubmit} from "../../models/payment-method-submit.model";


@Injectable({
  providedIn: 'root'
})
export class PaymentService {

  constructor(private http:HttpClient) {
  }

  private basePath = 'http://localhost:8080';

  createSubscription(): Observable<any> {
    let id = JSON.parse(localStorage.getItem("userData")).id;
    return this.http.post<any>(`${this.basePath}/create-subscription`, new SubscriptionRequest(id)).pipe(tap(it => console.log(it)));
  }

  submitPaymentMethod(token:string): Observable<any> {
    let id = JSON.parse(localStorage.getItem("userData")).id;
    return this.http.post<any>(`${this.basePath}/submit-payment-method`, new PaymentMethodSubmit(id, token))
  }

  cancelSubscription(): Observable<any> {
    let id = JSON.parse(localStorage.getItem("userData")).id;
    return this.http.post<any>(`${this.basePath}/cancel-subscription`, new SubscriptionRequest(id))
  }
}
