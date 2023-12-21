package personal.finance.summary.application;

import org.springframework.stereotype.Component;
import personal.finance.summary.domain.Currency;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class CurrencyManager {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final CurrencyProvider currencyProvider;
    public static Map<Currency, Double> currencies = new ConcurrentHashMap<>();

    public CurrencyManager(CurrencyProvider currencyProvider) {
        this.currencyProvider = currencyProvider;
        this.start();
    }

    private void start() {
        scheduler.scheduleAtFixedRate(
            this::updateExchangeRates, 0, 2, TimeUnit.HOURS);
    }

    private void updateExchangeRates() {
        Map<Currency, Double> latestRates = currencyProvider.getRates();
        currencies.clear();
        currencies.putAll(latestRates);
    }
}
