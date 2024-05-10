package personal.finance.tracking.summary.query;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import personal.finance.tracking.summary.infrastracture.external.ExchangeData;
import personal.finance.tracking.summary.infrastracture.external.StockApiProvider;
import personal.finance.tracking.summary.infrastracture.external.StockData;
import personal.finance.tracking.summary.infrastracture.external.TickerData;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin
public class StockExplorerController {

    private final StockApiProvider stockApiProvider;
    private final StockRepository stockRepository;
    private final ExchangeRepository exchangeRepository;
    private final TickerRepository tickerRepository;

    @GetMapping("/{exchange}/{ticker}")
    public List<StockData> fetchStockData(
        @PathVariable("exchange") String exchange,
        @PathVariable("ticker") String ticker) {
        return stockApiProvider.fetchForPeriod(LocalDate.now().minusYears(1), LocalDate.now(), exchange, ticker);
    }

    @GetMapping("/exchange_list")
    public List<ExchangeData> fetchExchangeList() throws IOException {
        List<ExchangeData> exchangeData = exchangeRepository.findAll();
        if (exchangeData.isEmpty()) {
            List<ExchangeData> fetchedExchangeList = stockApiProvider.fetchExchangeList();
            fetchedExchangeList.forEach(i -> i.setId(UUID.randomUUID()));
            exchangeRepository.saveAll(fetchedExchangeList);
            return fetchedExchangeList;
        }
        return exchangeData;
    }

    @GetMapping("/exchange_list/{exchange}/stocks")
    public List<TickerData> fetchTickersForExchange(@PathVariable("exchange") String exchange) throws IOException {
        ExchangeData exchangeData = exchangeRepository.findByCode(exchange).orElse(null);

        if (exchangeData != null && !exchangeData.tickers.isEmpty()) {
            return exchangeData.tickers.stream().toList();
        }

        if (exchangeData != null) {
            List<TickerData> tickerData = stockApiProvider.fetchStockPerExchange(exchange);
            exchangeData.tickers = new HashSet<>(tickerData);
            tickerData.forEach(ticker -> {
                ticker.exchangeData = exchangeData;
                ticker.setId(UUID.randomUUID());
            });
            tickerRepository.saveAll(tickerData);
            return exchangeData.tickers.stream().toList();
        } else {
            return stockApiProvider.fetchStockPerExchange(exchange);
        }
    }

}
