package personal.finance.tracking.summary.application.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import personal.finance.tracking.asset.application.AssetDTO;
import personal.finance.tracking.summary.domain.Currency;
import personal.finance.tracking.summary.domain.SummaryState;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
public class SummaryDTO {
    public UUID id;
    public UUID userId;
    public BigDecimal money;
    public Currency currency;
    public SummaryState state;
    public String date;
    public List<AssetDTO> assets;
}

