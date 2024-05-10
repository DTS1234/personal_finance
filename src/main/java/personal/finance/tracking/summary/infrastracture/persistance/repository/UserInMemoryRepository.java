package personal.finance.tracking.summary.infrastracture.persistance.repository;

import personal.finance.tracking.summary.domain.UserRepository;
import personal.finance.tracking.summary.domain.Currency;
import personal.finance.tracking.summary.infrastracture.persistance.entity.UserEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserInMemoryRepository implements UserRepository {

    private Map<UUID, UserEntity> map = new HashMap<>();

    @Override
    public Currency getCurrency(UUID userId) {
        UserEntity userEntity = map.get(userId);
        if (userEntity == null) {
            return Currency.EUR;
        }
        return userEntity.getCurrency();
    }

    @Override
    public Currency updateCurrency(String userId, Currency currency) {
        map.put(UUID.fromString(userId), new UserEntity(userId, currency));
        return currency;
    }
}
