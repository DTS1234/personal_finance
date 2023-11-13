package personal.finance.iam.infrastracture.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import personal.finance.iam.domain.PasswordResetToken;

import java.util.Optional;

public interface PasswordTokenRepositoryJpa extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
}
