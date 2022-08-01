package personal.finance.summary.domain.model;

import lombok.*;
import personal.finance.asset.item.ItemDomain;

import java.math.BigDecimal;
import java.util.List;

@Builder
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class AssetDomain {

    @Getter
    private Long id;
    @Getter
    private String name;
    @Getter
    private List<ItemDomain> items;
    @Getter
    private BigDecimal moneyValue;

}

