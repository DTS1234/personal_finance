package personal.finance.iam.query;

import org.springframework.data.jpa.repository.JpaRepository;
import personal.finance.iam.domain.UserId;
import personal.finance.iam.domain.UserSubscription;
import personal.finance.iam.domain.UserSubscriptionId;

import java.util.Optional;

public interface UserSubscriptionQueryRepository extends JpaRepository<UserSubscription, UserSubscriptionId> {

    Optional<UserSubscription> findByUserId(UserId userId);
}
