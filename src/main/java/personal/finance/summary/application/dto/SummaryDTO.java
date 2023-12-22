package personal.finance.summary.application.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import personal.finance.summary.domain.Currency;
import personal.finance.summary.domain.SummaryState;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
public class SummaryDTO {
    public Long id;
    public UUID userId;
    public BigDecimal money;
    public Currency currency;
    public SummaryState state;
    public String date;
    public List<AssetDTO> assets;
}

