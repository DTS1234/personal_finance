package personal.finance.tracking.summary.application.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
public class ItemDTO {
    public UUID id;
    public BigDecimal money;
    public String name;
    public BigDecimal quantity;
}
