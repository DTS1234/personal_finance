package personal.finance.payment.application;

import com.stripe.model.Subscription;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import personal.finance.Fixtures;
import personal.finance.common.events.FakeEventPublisher;
import personal.finance.iam.domain.SubscriptionType;
import personal.finance.payment.application.dto.SubscriptionCancelledConfirmationDTO;
import personal.finance.payment.domain.Customer;
import personal.finance.payment.domain.CustomerId;
import personal.finance.payment.domain.CustomerRepository;
import personal.finance.payment.domain.events.SubscriptionCancelled;
import personal.finance.payment.domain.events.SubscriptionCreated;
import personal.finance.payment.infrastracture.external.StripeService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static personal.finance.Fixtures.CUSTOMER_STRIPE_ID;

@DisplayNameGeneration(ReplaceUnderscores.class)
class PaymentServiceTest {

    private final StripeService mock = mock(StripeService.class);
    private final CustomerRepository customerRepository = new InMemoryCustomerRepository();
    private final FakeEventPublisher eventPublisher = new FakeEventPublisher();
    private final PaymentService paymentService = new PaymentService(customerRepository, mock, eventPublisher);

    @Test
    void should_create_a_subscription() throws Exception {
        // given user without subscription
        CustomerId customerId = CustomerId.random();
        customerRepository.save(new Customer(customerId, "email@test.com", CUSTOMER_STRIPE_ID, Fixtures.CUSTOMER_PAYMENT_ID, null));
        when(mock.getPriceId()).thenReturn("price_1");
        Subscription t = fakeSubscriptionWithIdOf("sub_1");
        when(mock.createSubscription(CUSTOMER_STRIPE_ID, "price_1")).thenReturn(t);

        // when subscription is created
        paymentService.createSubscription(customerId.getStringValue());

        // then subscription data is saved
        Customer customer = customerRepository.findById(customerId);
        assertThat(customer.getStripeSubscriptionId()).isEqualTo("sub_1");

        // and event is published
        assertThat(eventPublisher.publishedStore).containsOnly(
            new SubscriptionCreated(paymentService,
                SubscriptionType.PREMIUM,
                UUID.fromString(customerId.getStringValue()),
                LocalDate.now().plusMonths(1))
        );
    }

    @Test
    void should_cancel_a_subscription() throws Exception {
        // given user without subscription
        CustomerId customerId = CustomerId.random();
        customerRepository.save(new Customer(customerId, "email@test.com", CUSTOMER_STRIPE_ID, Fixtures.CUSTOMER_PAYMENT_ID, "subscription_id"));
        LocalDate expiryDate = LocalDate.now().plusDays(1);
        when(mock.cancelSubscription("subscription_id")).thenReturn(
            fakeSubscriptionWithIdAndExpiryDateOf("subscription_id", expiryDate)
        );

        // when subscription is cancelled
        SubscriptionCancelledConfirmationDTO dto = paymentService.cancelSubscription(customerId.getStringValue());

        // then confirmation is created
        assertThat(dto).isEqualTo(
            new SubscriptionCancelledConfirmationDTO(CUSTOMER_STRIPE_ID, "Subscription cancelled.", expiryDate)
        );

        // and event is published
        assertThat(eventPublisher.publishedStore).containsOnly(
            new SubscriptionCancelled(paymentService,
                customerId.getStringValue(),
                LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.SECONDS))
        );
    }

    private static Subscription fakeSubscriptionWithIdOf(String id) {
        Subscription t = new Subscription();
        t.setId(id);
        return t;
    }

    private static Subscription fakeSubscriptionWithIdAndExpiryDateOf(String id, LocalDate cancelDate) {
        Subscription t = new Subscription();
        t.setId(id);
        t.setCancelAt(cancelDate.toEpochSecond(LocalTime.now(), ZoneOffset.UTC));
        return t;
    }
}