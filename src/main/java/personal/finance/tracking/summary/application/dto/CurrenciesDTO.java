package personal.finance.tracking.summary.application.dto;

import org.apache.commons.lang3.tuple.Pair;
import personal.finance.tracking.summary.domain.Currency;

import java.util.Map;

public class CurrenciesDTO {

    public CurrenciesDTO(Map<Pair<Currency, Currency>, Double> rates) {
        this.rates = rates;
    }

    public Map<Pair<Currency, Currency>, Double> rates;
}
