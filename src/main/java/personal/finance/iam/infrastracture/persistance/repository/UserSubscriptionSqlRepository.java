package personal.finance.iam.infrastracture.persistance.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import personal.finance.iam.domain.UserId;
import personal.finance.iam.domain.UserSubscription;
import personal.finance.iam.domain.UserSubscriptionId;
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

    @Override
    public void delete(UserSubscriptionId id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public UserSubscription findByUserId(UserId userId) {
        return jpaRepository.findByUserId(userId).orElse(null);
    }
}
