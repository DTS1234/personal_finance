package personal.finance.summary.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import personal.finance.summary.SummaryState;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Builder
@AllArgsConstructor
public class SummaryDomain {

    @Getter
    private Long id;
    @Getter
    private SummaryState state;
    @Getter
    private List<AssetDomain> assets;
    @Getter
    private LocalDate date;
    @Getter
    @Setter
    private BigDecimal moneyValue;

    public void changeState(SummaryState state) {
        this.state = state;
    }

}
