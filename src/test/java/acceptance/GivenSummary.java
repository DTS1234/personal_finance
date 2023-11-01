package acceptance;

import personal.finance.summary.domain.model.SummaryState;
import personal.finance.summary.domain.model.Summary;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class GivenSummary {

    public static Summary summaryEntityInDraft() {
        return Summary.builder()
            .state(SummaryState.DRAFT)
            .assets(GivenAssets.assets())
            .date(LocalDateTime.now())
            .id(1L)
            .moneyValue(BigDecimal.valueOf(4201.67))
            .build();
    }

}
