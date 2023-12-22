package personal.finance.summary.infrastracture.persistance.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import personal.finance.summary.domain.UserRepository;
import personal.finance.summary.domain.Currency;
import personal.finance.summary.infrastracture.persistance.entity.UserEntity;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserSummaryRepositorySql implements UserRepository {

    private final UserSummaryRepositoryJpa repositoryJpa;

    @Override
    public Currency getCurrency(UUID userId) {
        UserEntity userEntity = repositoryJpa.findById(userId.toString()).orElse(null);
        return userEntity == null ? null : userEntity.getCurrency();
    }

    @Override
    public Currency updateCurrency(String userId, Currency currency) {
        UserEntity userEntity = repositoryJpa.findById(userId).orElse(null);
        if (userEntity == null) {
            repositoryJpa.save(new UserEntity(userId, currency));
            return currency;
        }
        userEntity.setCurrency(currency);
        repositoryJpa.save(userEntity);
        return currency;
    }
}
