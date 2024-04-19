package personal.finance.payment.application.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import personal.finance.iam.domain.event.UserRegistered;
import personal.finance.payment.domain.Customer;
import personal.finance.payment.domain.CustomerId;
import personal.finance.payment.infrastracture.external.StripeService;
import personal.finance.payment.infrastracture.persistance.CustomerRepositoryJpa;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserRegistrationListener {

    private final StripeService stripeService;
    private final CustomerRepositoryJpa customerRepositoryJpa;

    @EventListener
    public void onUserRegistered(UserRegistered event) {
        Customer customer = new Customer();
        customer.setId(new CustomerId(event.getId()));
        customer.setEmail(event.getEmail());
        Customer saved = customerRepositoryJpa.save(customer);
        log.info("Customer created with email: " + saved.getEmail());

        try {
            com.stripe.model.Customer stripeCustomer = stripeService.createStripeCustomer(event.getEmail());
            saved.setCustomerStripeId(stripeCustomer.getId());
            log.info("Customer stripe id assigned: " + saved.getCustomerStripeId());
        } catch (Exception e) {
            log.error("Failed to create a stripe customer.");
            e.printStackTrace();
        }
    }
}
