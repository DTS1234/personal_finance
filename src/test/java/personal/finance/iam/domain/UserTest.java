package personal.finance.iam.domain;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static personal.finance.iam.domain.SubscriptionType.PREMIUM;

@DisplayNameGeneration(ReplaceUnderscores.class)
class UserTest {


    @Test
    void premiumUserTest() {
        User user = User.create();
        UserSubscription userSubscription = new UserSubscription();
        userSubscription.setSubscriptionType(PREMIUM);

        LocalDate now = LocalDate.now();
        userSubscription.setStart(now.minusDays(1));
        userSubscription.setExpires(now.plusDays(1));

        user.setUserSubscription(userSubscription);

        assertThat(user.isPremium()).isTrue();
    }

    @Test
    void should_not_be_premium_if_subscription_expired() {
        User user = User.create();
        UserSubscription userSubscription = new UserSubscription();
        userSubscription.setSubscriptionType(PREMIUM);

        LocalDate now = LocalDate.now();
        userSubscription.setStart(now.minusDays(2));
        userSubscription.setExpires(now.minusDays(1));

        user.setUserSubscription(userSubscription);

        assertThat(user.isPremium()).isFalse();
    }

    @Test
    void should_not_be_premium_if_subscription_did_not_start() {
        User user = User.create();
        UserSubscription userSubscription = new UserSubscription();
        userSubscription.setSubscriptionType(PREMIUM);

        LocalDate now = LocalDate.now();
        userSubscription.setStart(now.plusDays(1));
        userSubscription.setExpires(now.plusDays(31));

        user.setUserSubscription(userSubscription);

        assertThat(user.isPremium()).isFalse();
    }
}