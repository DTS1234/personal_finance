package personal.finance.summary.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import personal.finance.asset.item.ItemDomain;
import personal.finance.summary.SummaryState;

import java.math.BigDecimal;
import java.util.List;

@Builder
@AllArgsConstructor
@EqualsAndHashCode
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

