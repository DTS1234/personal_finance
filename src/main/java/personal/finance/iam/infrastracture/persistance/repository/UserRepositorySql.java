package personal.finance.iam.infrastracture.persistance.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import personal.finance.iam.domain.User;
import personal.finance.iam.domain.UserRepository;

@RequiredArgsConstructor
@Component
public class UserRepositorySql implements UserRepository {

    private final UserRepositoryJpa repositoryJpa;

    @Override
    public User findByEmail(String email) {
        return repositoryJpa.findByEmail(email).orElse(null);
    }

    @Override
    public User save(User user) {
        return repositoryJpa.save(user);
    }

    @Override
    public void deleteByEmail(String email) {
        repositoryJpa.deleteByEmail(email);
    }


}
