package personal.finance.summary.application;

import personal.finance.summary.domain.Currency;

public interface UserRepository {
    Currency getCurrency(String userId);
    Currency updateCurrency(String userId, Currency currency);
}
