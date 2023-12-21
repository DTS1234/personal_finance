package personal.finance.summary.infrastracture.persistance.repository;

import personal.finance.summary.application.UserRepository;
import personal.finance.summary.domain.Currency;
import personal.finance.summary.infrastracture.persistance.entity.UserEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserInMemoryRepository implements UserRepository {

    private Map<UUID, UserEntity> map = new HashMap<>();

    @Override
    public Currency getCurrency(String userId) {
        return Currency.EUR;
    }

    @Override
    public Currency updateCurrency(String userId, Currency currency) {
        return map.put(UUID.fromString(userId), new UserEntity(userId, currency)).getCurrency();
    }
}
