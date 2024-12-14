package personal.finance.payment.application;

import com.stripe.model.Customer;
import com.stripe.model.Subscription;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import personal.finance.common.events.EventPublisher;
import personal.finance.iam.domain.SubscriptionType;
import personal.finance.payment.application.dto.SubscriptionCancelledConfirmationDTO;
import personal.finance.payment.domain.CustomerId;
import personal.finance.payment.domain.CustomerRepository;
import personal.finance.payment.domain.events.SubscriptionCancelled;
import personal.finance.payment.domain.events.SubscriptionCreated;
import personal.finance.payment.infrastracture.external.StripeService;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final CustomerRepository customerRepository;
    private final StripeService stripeService;
    private final EventPublisher applicationEventPublisher;

    public personal.finance.payment.domain.Customer submitPaymentMethod(String userId, String token) {
        personal.finance.payment.domain.Customer customerFound = customerRepository.findById(new CustomerId(UUID.fromString(userId)));

        if (customerFound == null) {
            String message = "Customer for " + userId + " not found.";
            log.error(message);
            throw new IllegalStateException(message);
        }

        Customer customer = stripeService.assignPaymentMethod(customerFound.getCustomerStripeId(), token);
        customerFound.setPaymentMethodId(customer.getDefaultSource());
        customerRepository.save(customerFound);

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

        if (customerFound.getPaymentMethodId() == null) {
            String message = "Customer has no payment method attached, cannot create a subscription";
            log.error(message);
            throw new IllegalStateException(message);
        }

        try {
            Subscription subscription = stripeService.createSubscription(customerFound.getCustomerStripeId(), stripeService.getPriceId());
            customerFound.setStripeSubscriptionId(subscription.getId());
            applicationEventPublisher.publishEvent(
                new SubscriptionCreated(this, SubscriptionType.PREMIUM, UUID.fromString(userId), getExpirationDate(subscription))
            );
            return subscription;
        } catch (Exception e) {
            String message = "Failed to create a subscription for " + customerFound;
            log.error(message);
            throw new IllegalStateException(message);
        }
    }

    public SubscriptionCancelledConfirmationDTO cancelSubscription(String userId) {
        personal.finance.payment.domain.Customer customerFound = customerRepository.findById(new CustomerId(UUID.fromString(userId)));
        if (customerFound == null) {
            String message = "Customer for " + userId + " not found.";
            log.error(message);
            throw new IllegalStateException(message);
        }

        Subscription cancelled = stripeService.cancelSubscription(customerFound.getStripeSubscriptionId());

        if (cancelled != null) {
            Long cancelAtLong = cancelled.getCancelAt();
            Instant instant = Instant.ofEpochSecond(cancelAtLong);
            LocalDateTime cancelAtPeriodEnd = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
            applicationEventPublisher.publishEvent(new SubscriptionCancelled(this, userId, cancelAtPeriodEnd));
            return new SubscriptionCancelledConfirmationDTO(customerFound.getCustomerStripeId(), "Subscription cancelled.", cancelAtPeriodEnd.toLocalDate());
        } else {
            return new SubscriptionCancelledConfirmationDTO(customerFound.getCustomerStripeId(), "Failed to cancel subscription.", null);
        }
    }

    private static LocalDate getExpirationDate(Subscription subscription) {
        if (subscription.getCurrentPeriodEnd() != null) {
            Instant instant = Instant.ofEpochSecond(subscription.getCurrentPeriodEnd());
            return LocalDate.ofInstant(instant, ZoneId.of("Z"));
        }
        return LocalDate.now().plusMonths(1);
    }
}
