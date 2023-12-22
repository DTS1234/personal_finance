package personal.finance.summary.application.dto;

import personal.finance.summary.domain.Currency;

public record UpdateCurrencyDTO(String userId, Currency currency) {

}
