package personal.finance.tracking.external;

import integration.IntegrationTest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import personal.finance.tracking.summary.infrastracture.external.ExchangeData;
import personal.finance.tracking.summary.infrastracture.external.StockApiProvider;
import personal.finance.tracking.summary.infrastracture.external.StockData;

import java.time.LocalDate;
import java.util.List;

@DisplayNameGeneration(ReplaceUnderscores.class)
class StockApiProviderTest extends IntegrationTest {
    
    private final StockApiProvider stockApiProvider = new StockApiProvider();
    
    @Test
    void stock_fetch_test() {
        LocalDate start = LocalDate.of(2024, 04, 20);
        LocalDate to = start.plusDays(6);
        List<StockData> stockData = stockApiProvider.fetchForPeriod(start, to, "US", "TSLA");
        System.out.println(stockData);
    }

    @Test
    void exchange_fetch_test() {
        List<ExchangeData> exchangeData = stockApiProvider.fetchExchangeList();
        System.out.println(exchangeData);
    }

}