package personal.finance.summary.application.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import personal.finance.summary.domain.SummaryState;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class SummaryDTO {
    public Long id;
    public Long userId;
    public BigDecimal money;
    public SummaryState state;
    public LocalDateTime date;
    public List<AssetDTO> assets;
}

