package personal.finance.summary.infrastracture.external;

import integration.IntegrationTest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;

@DisplayNameGeneration(ReplaceUnderscores.class)
class StockApiProviderTest extends IntegrationTest {
    
    private final StockApiProvider stockApiProvider = new StockApiProvider();
    
    @Test
    void test() {
        LocalDate start = LocalDate.of(2024, 04, 20);
        LocalDate to = start.plusDays(6);
        List<StockData> stockData = stockApiProvider.fetchForPeriod(start, to, "US", "TSLA");
        System.out.println(stockData);
    }

}