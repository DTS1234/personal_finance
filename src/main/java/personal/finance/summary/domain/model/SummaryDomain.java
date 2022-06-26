package personal.finance.summary.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import personal.finance.summary.SummaryState;
import personal.finance.summary.domain.state.State;

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
    private LocalDate localDate;
    @Getter
    private BigDecimal moneyValue;

    public void changeState(State state) {

    }

}
