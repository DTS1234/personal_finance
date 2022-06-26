package personal.finance.summary.output;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import personal.finance.summary.SummaryState;

import java.time.LocalDate;

@Builder
@Data
@AllArgsConstructor
public class Summary {

    private Long id;
    private double moneyValue;
    private LocalDate date;
    private SummaryState state;

    public Summary() {

    }


}
