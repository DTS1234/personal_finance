package personal.finance.iam.infrastracture.persistance.repository;

import personal.finance.iam.domain.User;
import personal.finance.iam.domain.UserRepository;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class InMemoryUserRepository implements UserRepository {

    private static final Map<Long, User> USERS = new HashMap<>();

    @Override
    public User findByEmail(String email) {
        return USERS.values().stream().filter(u -> u.getEmail().equals(email)).findFirst().orElse(null);
    }

    @Override
    public User save(User user) {
        user.setId(USERS.keySet().stream().max(Comparator.comparing(Long::longValue)).orElse(0L) + 1L);
        USERS.put(user.getId(), user);
        return USERS.get(user.getId());
    }

    @Override
    public void deleteByEmail(String email) {
        USERS.remove(findByEmail(email).getId());
    }

    public InMemoryUserRepository clear() {
        USERS.clear();
        return this;
    }
}
