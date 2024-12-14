package personal.finance.iam;

import personal.finance.iam.domain.UserSubscription;
import personal.finance.iam.domain.UserSubscriptionId;
import personal.finance.iam.domain.UserSubscriptionRepository;

import java.util.HashMap;
import java.util.Map;

public class InMemoryUserSubscriptionRepository implements UserSubscriptionRepository  {

    private final Map<UserSubscriptionId, UserSubscription> store = new HashMap<>();

    @Override
    public UserSubscription save(UserSubscription userSubscription) {
        store.put(userSubscription.getId(), userSubscription);
        return userSubscription;
    }

    @Override
    public void delete(UserSubscriptionId id) {
        store.remove(id);
    }

    @Override
    public UserSubscription findById(UserSubscriptionId id) {
        return store.get(id);
    }
}
