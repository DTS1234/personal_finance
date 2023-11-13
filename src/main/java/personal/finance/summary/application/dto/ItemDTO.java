package personal.finance.summary.application.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
public class ItemDTO {
    public Long id;
    public BigDecimal money;
    public String name;
    public BigDecimal quantity;
}
