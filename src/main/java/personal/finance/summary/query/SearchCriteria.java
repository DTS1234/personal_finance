package personal.finance.summary.application.query;

import lombok.Data;
import personal.finance.summary.domain.SummaryState;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class SearchCriteria {
    private UUID userId;
    private BigDecimal minMoneyValue;
    private BigDecimal maxMoneyValue;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private SummaryState state;
}
