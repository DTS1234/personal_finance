package personal.finance.payment.application;

import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Subscription;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import personal.finance.iam.domain.SubscriptionType;
import personal.finance.payment.domain.CustomerId;
import personal.finance.payment.domain.CustomerRepository;
import personal.finance.payment.domain.events.SubscriptionCreated;
import personal.finance.payment.infrastracture.external.StripeService;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final CustomerRepository customerRepository;
    private final StripeService stripeService;
    private final ApplicationEventPublisher applicationEventPublisher;

    public personal.finance.payment.domain.Customer submitPaymentMethod(String userId, String token) {
        personal.finance.payment.domain.Customer customerFound = customerRepository.findById(
            new CustomerId(UUID.fromString(userId)));

        if (customerFound == null) {
            String message = "Customer for " + userId + " not found.";
            log.error(message);
            throw new IllegalStateException(message);
        }

        Customer customer = stripeService.assignPaymentMethod(customerFound.getCustomerStripeId(), token);
        customerFound.setPaymentMethodId(customer.getDefaultSource());

        log.info("Payment method set for customer. " + customerFound.getPaymentMethodId());
        return customerFound;
    }

    public Subscription createSubscription(String userId) {
        personal.finance.payment.domain.Customer customerFound = customerRepository.findById(new CustomerId(UUID.fromString(userId)));

        if (customerFound == null) {
            String message = "Customer for " + userId + " not found.";
            log.error(message);
            throw new IllegalStateException(message);
        }

        try {
            Subscription subscription = stripeService.createSubscription(customerFound.getCustomerStripeId(),
                stripeService.getPriceId());
            applicationEventPublisher.publishEvent(new SubscriptionCreated(this, SubscriptionType.PREMIUM, UUID.fromString(userId)));
            return subscription;
        } catch (Exception e) {
            String message = "Failed to create a subscription for " + customerFound;
            log.error(message);
            e.printStackTrace();
            throw new IllegalStateException(message);
        }
    }
}
