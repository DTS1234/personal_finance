package personal.finance.iam.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import personal.finance.iam.domain.User;
import personal.finance.iam.domain.UserId;
import personal.finance.iam.domain.UserRepository;
import personal.finance.iam.domain.UserSubscriptionRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class SubscriptionExpiryScheduler {

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final UserSubscriptionRepository userSubscriptionRepository;
    private final UserRepository userRepository;

    public void schedule(LocalDateTime subscriptionEndDate, UserId userId) {
        log.info("Scheduling subscription expiry for user: {}. Current date: {}. Expiry date: {}", userId, LocalDateTime.now(), subscriptionEndDate);
        long delay = ChronoUnit.SECONDS.between(LocalDateTime.now(), subscriptionEndDate);
        if (delay > 0) {
            scheduler.schedule(() -> {
                User userFound = userRepository.findById(UserId.fromString(userId.toString()));
                if (userFound == null) {
                    log.error("User not found for subscription expiry. User: {}", userId);
                    return;
                }
                userSubscriptionRepository.delete(userFound.getUserSubscription().getId());
                log.info("Subscription expired for user: {}", userId);
            }, delay, TimeUnit.SECONDS);
        }
    }

}
