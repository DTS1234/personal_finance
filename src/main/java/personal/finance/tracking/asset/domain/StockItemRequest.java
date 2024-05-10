package personal.finance.tracking.asset.domain;

import java.math.BigDecimal;

public record StockItemRequest(String ticker, BigDecimal purchasePrice, BigDecimal currentPrice, String name,
                               BigDecimal quantity) {

}
