package personal.finance.summary.domain;

import java.util.UUID;

public interface UserRepository {
    Currency getCurrency(UUID userId);
    Currency updateCurrency(String userId, Currency currency);
}
