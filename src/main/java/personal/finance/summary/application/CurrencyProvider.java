package personal.finance.summary.application;

import personal.finance.summary.domain.Currency;

import java.util.Map;

public interface CurrencyProvider {

    Map<Currency, Double> getRates();
}
