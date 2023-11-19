package personal.finance.iam.infrastracture.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import personal.finance.iam.domain.User;
import personal.finance.iam.domain.UserId;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepositoryJpa extends JpaRepository<User, UserId> {

    void deleteByUserInformationEmail(@NonNull String email);

    Optional<User> findByUserInformationEmail(String email);
}
