package personal.finance.iam.application.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import personal.finance.iam.application.SubscriptionExpiryScheduler;
import personal.finance.iam.domain.SubscriptionStatus;
import personal.finance.iam.domain.UserId;
import personal.finance.iam.domain.UserSubscription;
import personal.finance.iam.domain.UserSubscriptionId;
import personal.finance.iam.domain.UserSubscriptionRepository;
import personal.finance.payment.domain.events.SubscriptionCancelled;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class SubscriptionCancelledListener {

    private final SubscriptionExpiryScheduler subscriptionExpiryScheduler;
    private final UserSubscriptionRepository userSubscriptionRepository;

    @EventListener
    public void onUserSubscriptionCancelled(SubscriptionCancelled event) {
        UserSubscription subscription = userSubscriptionRepository.findByUserId(new UserId(UUID.fromString(event.userId)));

        if (subscription == null) {
            log.error("Subscription not found for user: {}", event.userId);
            throw new IllegalStateException("Subscription not found for user: " + event.userId);
        }

        subscription.setStatus(SubscriptionStatus.CANCELLED);
        userSubscriptionRepository.save(subscription);

        subscriptionExpiryScheduler.schedule(event.expiresAt, UserId.fromString(event.userId));
    }

}
