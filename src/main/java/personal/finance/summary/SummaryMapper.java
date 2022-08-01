package personal.finance.summary;

import personal.finance.asset.AssetMapper;
import personal.finance.summary.domain.model.SummaryDomain;
import personal.finance.summary.output.Summary;
import personal.finance.summary.persistance.SummaryEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

public class SummaryMapper {

    public static SummaryEntity toEntity(SummaryDomain summaryDomain) {
        SummaryEntity build = SummaryEntity.builder()
                .state(summaryDomain.getState())
                .date(summaryDomain.getDate())
                .id(summaryDomain.getId())
                .moneyValue(Optional.ofNullable(summaryDomain.getMoneyValue()).orElse(BigDecimal.ZERO))
                .assets(Optional.ofNullable(summaryDomain.getAssets()).orElse(new ArrayList<>()).stream().map(AssetMapper::toEntity).collect(Collectors.toList()))
                .build();
        build.getAssets().forEach(asset ->
                asset.setSummary(build));
        return build;
    }

    public static SummaryEntity toEntity(Summary summary) {
        return SummaryEntity.builder()
                .state(summary.getState())
                .date(summary.getDate())
                .id(summary.getId())
                .moneyValue(Optional.ofNullable(summary.getMoneyValue()).orElse(BigDecimal.ZERO))
                .assets(Optional.ofNullable(summary.getAssets()).orElse(new ArrayList<>()))
                .build();
    }

    public static Summary toSummary(SummaryEntity summaryEntity) {
        return Summary.builder()
                .id(summaryEntity.getId())
                .date(summaryEntity.getDate())
                .moneyValue(Optional.ofNullable(summaryEntity.getMoneyValue()).orElse(BigDecimal.ZERO))
                .assets(summaryEntity.getAssets())
                .state(summaryEntity.getState())
                .build();
    }

    public static Summary toSummary(SummaryDomain summaryDomain) {
        return Summary.builder()
                .id(summaryDomain.getId())
                .date(summaryDomain.getDate())
                .moneyValue(Optional.ofNullable(summaryDomain.getMoneyValue()).orElse(BigDecimal.ZERO))
                .assets(summaryDomain.getAssets().stream().map(AssetMapper::toEntity).collect(Collectors.toList()))
                .state(summaryDomain.getState())
                .build();
    }

    public static SummaryDomain toDomain(SummaryEntity summaryEntity) {
        return SummaryDomain.builder()
                .id(summaryEntity.getId())
                .date(summaryEntity.getDate())
                .moneyValue(Optional.ofNullable(summaryEntity.getMoneyValue()).orElse(BigDecimal.ZERO))
                .assets(summaryEntity.getAssets().stream().map(AssetMapper::toDomain).collect(Collectors.toList()))
                .state(summaryEntity.getState())
                .build();
    }
}
