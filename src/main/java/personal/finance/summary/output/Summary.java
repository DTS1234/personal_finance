package personal.finance.summary.output;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import personal.finance.asset.Asset;
import personal.finance.summary.SummaryState;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
public class Summary {

    private Long id;
    private BigDecimal moneyValue;
    private LocalDateTime date;
    private SummaryState state;
    private List<Asset> assets;

    public Summary() {

    }


}
