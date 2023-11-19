package personal.finance.iam.infrastracture.persistance.repository;

import personal.finance.iam.domain.User;
import personal.finance.iam.domain.UserId;
import personal.finance.iam.domain.UserRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InMemoryUserRepository implements UserRepository {

    private static final Map<UUID, User> USERS = new HashMap<>();

    @Override
    public User findByEmail(String email) {
        return USERS.values().stream().filter(u -> u.getUserInformation().getEmail().equals(email)).findFirst()
            .orElse(null);
    }

    @Override
    public User save(User user) {
        user.setId(UserId.random());
        USERS.put(user.getId().value, user);
        return USERS.get(user.getId().value);
    }

    @Override
    public void deleteByEmail(String email) {
        USERS.remove(findByEmail(email).getId().value);
    }

    public InMemoryUserRepository clear() {
        USERS.clear();
        return this;
    }
}
