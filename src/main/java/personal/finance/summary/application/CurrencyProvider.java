package personal.finance.summary.application;

import org.apache.commons.lang3.tuple.Pair;
import personal.finance.summary.domain.Currency;

import java.util.Map;

public interface CurrencyProvider {

    Map<Pair<Currency, Currency>, Double> getRates();
}
