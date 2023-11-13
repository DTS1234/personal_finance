package personal.finance;

import personal.finance.summary.domain.SummaryState;
import personal.finance.summary.domain.Money;
import personal.finance.summary.domain.Summary;
import personal.finance.summary.domain.SummaryId;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class GivenSummary {

    public static Summary summaryEntityInDraft() {
        return Summary.builder()
            .state(SummaryState.DRAFT)
            .assets(GivenAssets.assets())
            .date(LocalDateTime.now())
            .id(new SummaryId(1L))
            .money(new Money(BigDecimal.valueOf(4201.67)))
            .build();
    }

}
