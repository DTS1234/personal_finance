package personal.finance.iam.infrastracture.persistance.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import personal.finance.iam.domain.UserSubscription;
import personal.finance.iam.domain.UserSubscriptionRepository;
import personal.finance.iam.query.UserSubscriptionQueryRepository;

@Component
@RequiredArgsConstructor
public class UserSubscriptionSqlRepository implements UserSubscriptionRepository {
    private final UserSubscriptionQueryRepository jpaRepository;

    @Override
    public UserSubscription save(UserSubscription userSubscription) {
        return jpaRepository.save(userSubscription);
    }
}
