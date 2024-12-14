package personal.finance.iam;

import personal.finance.iam.domain.User;
import personal.finance.iam.domain.UserId;
import personal.finance.iam.domain.UserRepository;

import java.util.HashMap;
import java.util.Map;

public class InMemoryUserRepository implements UserRepository {

    private final Map<UserId, User> store = new HashMap<>();
    private final Map<String, User> emailStore = new HashMap<>();

    @Override
    public User findByEmail(String email) {
        return emailStore.get(email);
    }

    @Override
    public User save(User user) {
        store.put(user.getId(), user);
        emailStore.put(user.getUserInformation().getEmail(), user);
        return user;
    }

    @Override
    public void deleteByEmail(String email) {
        User user = emailStore.remove(email);
        if (user != null) {
            store.remove(user.getId());
        }
    }

    @Override
    public User findById(UserId userId) {
        return store.get(userId);
    }
}
