package personal.finance.tracking.asset.application;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import personal.finance.tracking.asset.domain.AssetType;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
public class AssetDTO {
    public UUID id;
    public BigDecimal money;
    public List<ItemDTO> items;
    public String name;
    public AssetType type;
    public UUID summaryId;
}
