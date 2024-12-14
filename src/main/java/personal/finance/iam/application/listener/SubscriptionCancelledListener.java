package personal.finance.iam.application.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import personal.finance.iam.domain.User;
import personal.finance.iam.domain.UserId;
import personal.finance.iam.domain.UserRepository;
import personal.finance.iam.domain.UserSubscription;
import personal.finance.iam.domain.UserSubscriptionRepository;
import personal.finance.iam.domain.UserSubscriptionId;
import personal.finance.payment.domain.events.SubscriptionCreated;

@Component
@RequiredArgsConstructor
@Slf4j
public class SubscriptionCreatedListener {

    private final UserRepository userRepository;
    private final UserSubscriptionRepository userSubscriptionRepo;

    @EventListener
    public void onUserSubscribed(SubscriptionCreated event) {
        UserSubscription userSubscription = saveSubscriptionForUser(event);
    }

    public UserSubscription saveSubscriptionForUser(SubscriptionCreated subscriptionCreated) {
        User byId = userRepository.findById(new UserId(subscriptionCreated.userId));
        if (byId == null) {
            throw new IllegalStateException("User does not exist");
        }

        UserSubscription userSubscription = new UserSubscription();
        userSubscription.setUserSubscriptionId(UserSubscriptionId.random());
        userSubscription.setUser(byId);
        userSubscription.setStart(subscriptionCreated.start);
        userSubscription.setExpires(subscriptionCreated.expiresAt);
        userSubscription.setSubscriptionType(subscriptionCreated.type);

        return userSubscriptionRepo.save(userSubscription);
    }

}
