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
        System.out.println(query);
        ResponseEntity<StockData[]> forEntity = restTemplate.getForEntity(query, StockData[].class);
        return List.of(forEntity.getBody());
    }

}
