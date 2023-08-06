package personal.finance.summary.asset.item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import personal.finance.summary.domain.model.AssetDomain;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@EqualsAndHashCode
public class ItemDomain {

    @Getter
    private Long id;
    @Getter
    private BigDecimal moneyValue;
    @Getter
    private String name;
    @Getter
    private BigDecimal quantity;

}
