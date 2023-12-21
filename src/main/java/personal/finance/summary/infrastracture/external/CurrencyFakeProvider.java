package personal.finance.summary.infrastracture.external;

import lombok.extern.slf4j.Slf4j;
import personal.finance.summary.application.CurrencyProvider;
import personal.finance.summary.domain.Currency;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class CurrencyFakeProvider implements CurrencyProvider {

    @Override
    public Map<Currency, Double> getRates() {
        Map<Currency, Double> currencies = new ConcurrentHashMap<>();
        currencies.put(Currency.EUR, 1.0);
        currencies.put(Currency.PLN, 4.5);
        currencies.put(Currency.USD, 0.89);
        log.info("NEW rates fetched from FAKE provider: " + currencies);
        return currencies;
    }
}
