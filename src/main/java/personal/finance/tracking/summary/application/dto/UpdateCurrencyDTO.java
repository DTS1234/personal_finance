package personal.finance.tracking.summary.application.dto;

import personal.finance.tracking.summary.domain.Currency;

public record UpdateCurrencyDTO(String userId, Currency currency) {

}
