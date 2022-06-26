package personal.finance.summary;

import personal.finance.asset.AssetMapper;
import personal.finance.summary.domain.model.SummaryDomain;
import personal.finance.summary.persistance.SummaryEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

public class SummaryMapper {

    public static SummaryEntity toEntity(SummaryDomain summaryDomain) {
        return SummaryEntity.builder()
                .state(summaryDomain.getState())
                .date(summaryDomain.getLocalDate())
                .id(summaryDomain.getId())
                .moneyValue(Optional.ofNullable(summaryDomain.getMoneyValue()).map(BigDecimal::doubleValue).orElse(0.00))
                .assets(Optional.ofNullable(summaryDomain.getAssets()).orElse(new ArrayList<>()).stream().map(AssetMapper::toEntity).collect(Collectors.toList()))
                .build();
    }

}
