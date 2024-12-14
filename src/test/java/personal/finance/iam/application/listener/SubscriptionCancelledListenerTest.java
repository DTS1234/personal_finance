package personal.finance.iam.application.listener;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import personal.finance.Fixtures;
import personal.finance.iam.InMemoryUserSubscriptionRepository;
import personal.finance.iam.application.SubscriptionExpiryScheduler;
import personal.finance.iam.domain.SubscriptionStatus;
import personal.finance.iam.domain.User;
import personal.finance.iam.domain.UserId;
import personal.finance.iam.domain.UserSubscription;
import personal.finance.iam.domain.UserSubscriptionId;
import personal.finance.iam.domain.UserSubscriptionRepository;
import personal.finance.iam.infrastracture.persistance.repository.InMemoryUserRepository;
import personal.finance.payment.domain.events.SubscriptionCancelled;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(ReplaceUnderscores.class)
class SubscriptionCancelledListenerTest {

    private final SubscriptionExpiryScheduler subscriptionExpiryScheduler = new SubscriptionExpiryScheduler(new InMemoryUserSubscriptionRepository(),
        new InMemoryUserRepository());
    private final UserSubscriptionRepository repository = new InMemoryUserSubscriptionRepository();
    private final SubscriptionCancelledListener listener = new SubscriptionCancelledListener(subscriptionExpiryScheduler, repository);

    @Test
    void should_put_subscription_to_cancelled_status() {
        // given
        User user = new User();
        user.setId(UserId.fromString(Fixtures.USER_ID));
        // and
        UserSubscriptionId id = UserSubscriptionId.random();
        UserSubscription userSubscription = new UserSubscription();
        userSubscription.setUserSubscriptionId(id);
        userSubscription.setUser(user);
        // and
        repository.save(userSubscription);

        // when
        listener.onUserSubscriptionCancelled(new SubscriptionCancelled(this, Fixtures.USER_ID, LocalDateTime.now()));

        // then
        assertThat(repository.findByUserId(UserId.fromString(Fixtures.USER_ID)).getStatus() == SubscriptionStatus.CANCELLED).isTrue();
    }

}