package personal.finance.summary.infrastracture.external;

import java.math.BigDecimal;
import java.time.LocalDate;

public record StockData(LocalDate date, BigDecimal open, BigDecimal high, BigDecimal low, BigDecimal close,
                        BigDecimal adjusted_close, int volume) {

}
