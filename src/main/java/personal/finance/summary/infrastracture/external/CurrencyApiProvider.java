package personal.finance.summary.infrastracture.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import personal.finance.summary.application.CurrencyProvider;
import personal.finance.summary.domain.Currency;

import java.time.LocalDate;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class CurrencyApiProvider implements CurrencyProvider {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    public static final String API_KEY = "8d1ccca1fa427824d0bc41d70e8a9ad5";


    @Override
    public Map<Currency, Double> getRates() {

        String url = "http://api.exchangeratesapi.io/v1/";
        String query = getDateFormat() + "?access_key=" + API_KEY + "&symbols=PLN,EUR,USD";

        String result = restTemplate.getForObject(url + query, String.class);

        try {
            Map<Currency, Double> rates = objectMapper.readValue(result, ApiResponse.class).rates;
            log.info("Rates fetched from API : " + rates);
            return rates;
        } catch (JsonProcessingException jsonProcessingException) {
            log.error("ERROR while reading rates response.\n" + jsonProcessingException.getMessage());
            return new CurrencyFakeProvider().getRates();
        }
    }

    private String getDateFormat() {
        LocalDate now = LocalDate.now();
        return String.format("%s-%s-%S", now.getYear(), now.getMonthValue(), now.getDayOfMonth());
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class ApiResponse {

        public Map<Currency, Double> rates;

    }
}
