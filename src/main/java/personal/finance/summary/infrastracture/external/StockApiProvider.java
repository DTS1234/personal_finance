package personal.finance.summary.infrastracture.external;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Component
public class StockApiProvider {
    private static final String BASE_URL = "https://eodhd.com/api/eod/";
    private static final String API_KEY = "658aef2bda56c7.96723277";

    public List<StockData> fetchForPeriod(LocalDate from, LocalDate to, String exchange, String ticker) {
        RestTemplate restTemplate = new RestTemplate();
        String query = BASE_URL + ticker + "." + exchange + "?from=" + from + "&to=" + to + "&period=d&api_token=" + API_KEY + "&fmt=json";
        ResponseEntity<StockData[]> forEntity = restTemplate.getForEntity(query, StockData[].class);
        return List.of(forEntity.getBody());
    }

    public List<ExchangeData> fetchExchangeList() {
        String query = "https://eodhd.com/api/exchanges-list/?api_token=658aef2bda56c7.96723277&fmt=json";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ExchangeData[]> forEntity = restTemplate.getForEntity(query, ExchangeData[].class);
        return List.of(forEntity.getBody());
    }

}
