package personal.finance.iam.infrastracture.persistance.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import personal.finance.iam.domain.VerificationToken;
import personal.finance.iam.domain.VerificationTokenRepository;

@Component
@RequiredArgsConstructor
public class VerificationTokenRepositorySql implements VerificationTokenRepository {

    private final VerificationTokenRepositoryJpa repository;

    @Override
    public VerificationToken findByToken(String token) {
        return repository.findByToken(token);
    }

    @Override
    public VerificationToken save(VerificationToken token) {
        return repository.save(token);
    }

    @Override
    public VerificationToken findByUser(String email) {
        return repository.findByUser(email);
    }
}
