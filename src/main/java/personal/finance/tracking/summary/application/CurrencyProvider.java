package personal.finance.tracking.summary.application;

import org.apache.commons.lang3.tuple.Pair;
import personal.finance.tracking.summary.domain.Currency;

import java.util.Map;

public interface CurrencyProvider {

    Map<Pair<Currency, Currency>, Double> getRates();
}
