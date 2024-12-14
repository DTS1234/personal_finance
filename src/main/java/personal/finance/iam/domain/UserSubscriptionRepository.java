package personal.finance.iam.domain;

public interface UserSubscriptionRepository {
    UserSubscription save(UserSubscription userSubscription);
    void delete(UserSubscriptionId id);
    UserSubscription findByUserId(UserId userId);
}
