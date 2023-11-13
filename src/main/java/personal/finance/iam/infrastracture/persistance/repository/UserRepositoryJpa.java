package personal.finance.iam.infrastracture.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import personal.finance.iam.domain.User;

import java.util.Optional;

@Repository
public interface UserRepositoryJpa extends JpaRepository<User, Long> {

    void deleteByEmail(@NonNull String email);

    Optional<User> findByEmail(String email);
}
