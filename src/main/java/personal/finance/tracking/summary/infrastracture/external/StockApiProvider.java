package personal.finance.tracking.summary.infrastracture.external;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Component
public class StockApiProvider {

    private static final String BASE_URL = "https://eodhd.com/api/eod/";
    private static final String API_KEY = "658aef2bda56c7.96723277";

    public List<StockData> fetchForPeriod(LocalDate from, LocalDate to, String exchange, String ticker) {
        RestTemplate restTemplate = new RestTemplate();
        String query = BASE_URL + ticker + "." + exchange + "?api_token=" + API_KEY + "&fmt=json";
        ResponseEntity<StockData[]> response = restTemplate.getForEntity(query, StockData[].class);
        StockData[] body = Optional.ofNullable(response.getBody()).orElse(new StockData[0]);
        return List.of(body);
    }

    public List<ExchangeData> fetchExchangeList() {
        String query = "https://eodhd.com/api/exchanges-list/?api_token=658aef2bda56c7.96723277&fmt=json";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ExchangeData[]> response = restTemplate.getForEntity(query, ExchangeData[].class);
        ExchangeData[] body = Optional.ofNullable(response.getBody()).orElse(new ExchangeData[0]);
        return List.of(body);
    }

    public List<TickerData> fetchStockPerExchange(String exchangeCode) {
        String query = "https://eodhd.com/api/exchange-symbol-list/" + exchangeCode
            + "?api_token=658aef2bda56c7.96723277&fmt=json";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<TickerData[]> response = restTemplate.getForEntity(query, TickerData[].class);
        TickerData[] body = Optional.ofNullable(response.getBody()).orElse(new TickerData[0]);
        return List.of(body);
    }

    public BigDecimal fetchPriceByTicker(String ticker) {
        String query = "https://eodhd.com/api/search/" + ticker + "?api_token=658aef2bda56c7.96723277&fmt=json";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<SearchStockData[]> response = restTemplate.getForEntity(query, SearchStockData[].class);
        return Optional.ofNullable(response.getBody()).flatMap(
            body -> Arrays.stream(body).findFirst().map(SearchStockData::getPreviousClose)
        ).orElseThrow(()->new IllegalStateException("Unable to fetch data for: " + ticker));
    }

    public List<SearchStockData> fetchSearchStockData(String query) {
        String url = "https://eodhd.com/api/search/" + query + "?api_token=658aef2bda56c7.96723277&fmt=json";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<SearchStockData[]> response = restTemplate.getForEntity(url, SearchStockData[].class);
        return Optional.ofNullable(response.getBody()).map(r -> Arrays.stream(r).toList()).orElse(Collections.emptyList());
    }

}
