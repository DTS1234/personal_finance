package personal.finance.summary.infrastracture.external;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import personal.finance.summary.application.CurrencyProvider;
import personal.finance.summary.domain.Currency;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class CurrencyFakeProvider implements CurrencyProvider {

    @Override
    public Map<Pair<Currency, Currency>, Double> getRates() {
        Map<Pair<Currency, Currency>, Double> currencies = new ConcurrentHashMap<>();
        currencies.put(Pair.of(Currency.EUR, Currency.EUR), 1.0);
        currencies.put(Pair.of(Currency.PLN, Currency.EUR), 4.5);
        currencies.put(Pair.of(Currency.USD, Currency.EUR), 1.10);

        currencies.put(Pair.of(Currency.EUR, Currency.USD), 0.91);
        currencies.put(Pair.of(Currency.PLN, Currency.USD), 3.95);
        currencies.put(Pair.of(Currency.USD, Currency.USD), 1.0);

        currencies.put(Pair.of(Currency.EUR, Currency.PLN), 0.23);
        currencies.put(Pair.of(Currency.PLN, Currency.PLN), 1.0);
        currencies.put(Pair.of(Currency.USD, Currency.PLN), 0.25);

        log.info("NEW rates fetched from FAKE provider: " + currencies);
        return currencies;
    }
}
