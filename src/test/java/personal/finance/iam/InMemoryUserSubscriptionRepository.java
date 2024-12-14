package personal.finance.iam;

import personal.finance.iam.domain.UserId;
import personal.finance.iam.domain.UserSubscription;
import personal.finance.iam.domain.UserSubscriptionId;
import personal.finance.iam.domain.UserSubscriptionRepository;

import java.util.HashMap;
import java.util.Map;

public class InMemoryUserSubscriptionRepository implements UserSubscriptionRepository  {

    private final Map<UserId, UserSubscription> store = new HashMap<>();
    private final Map<UserSubscriptionId, UserSubscription> storeSub = new HashMap<>();

    @Override
    public UserSubscription save(UserSubscription userSubscription) {
        store.put(userSubscription.getUser().getId(), userSubscription);
        storeSub.put(userSubscription.getId(), userSubscription);
        return userSubscription;
    }

    @Override
    public void delete(UserSubscriptionId id) {
        storeSub.remove(id);
    }

    @Override
    public UserSubscription findByUserId(UserId id) {
        return store.get(id);
    }
}
