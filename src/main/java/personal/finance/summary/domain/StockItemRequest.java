package personal.finance.summary.domain;

import java.math.BigDecimal;

public record StockItemRequest(String ticker, BigDecimal purchasePrice, BigDecimal currentPrice, String name,
                               BigDecimal quantity) {

}
