package personal.finance.iam.query;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import personal.finance.iam.domain.UserId;
import personal.finance.iam.domain.UserSubscription;

import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class UserQueryController {

    private final UserSubscriptionQueryRepository userRepository;

    @GetMapping("/{userId}/subscription")
    public UserSubscriptionDTO userSubscription(@PathVariable("userId") UUID userId) {
        Optional<UserSubscription> optionalUserSubscription = userRepository.findByUserId(new UserId(userId));
        return optionalUserSubscription.map(it -> new UserSubscriptionDTO(
                it.getSubscriptionType(),
                it.getExpires(),
                it.getStart())
            ).orElseThrow(() -> new IllegalStateException("Subscription not found for user: " + userId));
    }

}
