package personal.finance.summary.domain;

import personal.finance.summary.domain.Currency;

import java.util.UUID;

public interface UserRepository {
    Currency getCurrency(UUID userId);
    Currency updateCurrency(String userId, Currency currency);
}
