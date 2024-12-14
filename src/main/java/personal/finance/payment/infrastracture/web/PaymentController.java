package personal.finance.payment.infrastracture.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import personal.finance.payment.application.PaymentService;
import personal.finance.payment.application.dto.SubmitPaymentMethodRequest;
import personal.finance.payment.application.dto.SubscriptionCancelledConfirmationDTO;
import personal.finance.payment.application.dto.SubscriptionRequestDTO;
import personal.finance.payment.domain.Subscription;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create-subscription")
    public Subscription createSubscription(@RequestBody SubscriptionRequestDTO subscriptionRequest) {
        com.stripe.model.Subscription subscription = paymentService.createSubscription(subscriptionRequest.userId);
        return new Subscription(subscription.getCustomer());
    }

    @PostMapping("/submit-payment-method")
    public personal.finance.payment.domain.Customer submitPaymentMethod(@RequestBody SubmitPaymentMethodRequest paymentMethodRequest) {
        return paymentService.submitPaymentMethod(paymentMethodRequest.userId, paymentMethodRequest.token);
    }

    @PostMapping("/cancel-subscription")
    public SubscriptionCancelledConfirmationDTO cancelSubscription(@RequestBody SubscriptionRequestDTO subscriptionRequest) {
        return paymentService.cancelSubscription(subscriptionRequest.userId);
    }
}
