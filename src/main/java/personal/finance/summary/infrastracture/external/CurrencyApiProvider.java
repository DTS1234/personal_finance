package personal.finance.summary.infrastracture.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import personal.finance.summary.application.CurrencyProvider;
import personal.finance.summary.domain.Currency;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class CurrencyApiProvider implements CurrencyProvider {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    public static final String API_KEY = "8d1ccca1fa427824d0bc41d70e8a9ad5";


    @Override
    public Map<Pair<Currency, Currency>, Double> getRates() {

        String query1 = "https://api.currencyapi.com/v3/latest?apikey=cur_live_HBoSFSA3sm8wt61qQiVSOw1jcaA3TSdEwlhyjfFm&base_currency=EUR&currencies=PLN,USD,EUR";
        String query2 = "https://api.currencyapi.com/v3/latest?apikey=cur_live_HBoSFSA3sm8wt61qQiVSOw1jcaA3TSdEwlhyjfFm&base_currency=USD&currencies=PLN,USD,EUR";
        String query3 = "https://api.currencyapi.com/v3/latest?apikey=cur_live_HBoSFSA3sm8wt61qQiVSOw1jcaA3TSdEwlhyjfFm&base_currency=PLN&currencies=PLN,USD,EUR";


        String resultEuro = restTemplate.getForObject(query1, String.class);
        String resultUSD = restTemplate.getForObject(query2, String.class);
        String resultPLN = restTemplate.getForObject(query3, String.class);
        System.out.println(resultEuro);
        try {
            Map<Currency, Double> ratesEuro = new HashMap<>();
            Map<Currency, Double> ratesUsd = new HashMap<>();
            Map<Currency, Double> ratesPln = new HashMap<>();

            ApiResponse eur = objectMapper.readValue(resultEuro, ApiResponse.class);
            eur.data.values().forEach(d -> {
                ratesEuro.put(Currency.valueOf(d.code), d.value);
            });

            ApiResponse pln = objectMapper.readValue(resultPLN, ApiResponse.class);
            pln.data.values().forEach(d -> {
                ratesPln.put(Currency.valueOf(d.code), d.value);
            });

            ApiResponse usd = objectMapper.readValue(resultUSD, ApiResponse.class);
            usd.data.values().forEach(d -> {
                ratesUsd.put(Currency.valueOf(d.code), d.value);
            });

            Map<Pair<Currency, Currency>, Double> euroMap = new HashMap<>();
            ratesEuro.forEach((k, v) -> {
                euroMap.put(Pair.of(k, Currency.EUR), v);
            });

            Map<Pair<Currency, Currency>, Double> usdMap = new HashMap<>();
            ratesUsd.forEach((k, v) -> {
                usdMap.put(Pair.of(k, Currency.USD), v);
            });

            Map<Pair<Currency, Currency>, Double> plnMap = new HashMap<>();
            ratesPln.forEach((k, v) -> {
                plnMap.put(Pair.of(k, Currency.PLN), v);
            });

            log.info("Rates fetched from API : " + ratesEuro);
            euroMap.putAll(plnMap);
            usdMap.putAll(euroMap);
            return usdMap;

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

        public Map<Currency, CurrencyAPIData> data;

    }

    static class CurrencyAPIData {
        public String code;
        public Double value;
    }
}
