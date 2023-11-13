package personal.finance.iam.infrastracture.persistance.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import personal.finance.iam.domain.PasswordResetToken;
import personal.finance.iam.domain.PasswordResetTokenRepository;

@Component
@RequiredArgsConstructor
public class PasswordTokenRepositorySql implements PasswordResetTokenRepository {

    private final PasswordTokenRepositoryJpa repositoryJpa;

    @Override
    public PasswordResetToken save(PasswordResetToken token) {
        return repositoryJpa.save(token);
    }

    @Override
    public PasswordResetToken findByToken(String token) {
        return repositoryJpa.findByToken(token).orElse(null);
    }
}
