package personal.finance.summary.application.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class AssetDTO {
    public Long id;
    public BigDecimal money;
    public List<ItemDTO> items;
    public String name;
}
