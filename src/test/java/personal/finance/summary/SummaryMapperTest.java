package personal.finance.summary;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import personal.finance.asset.Asset;
import personal.finance.asset.item.Item;
import personal.finance.asset.item.ItemDomain;
import personal.finance.summary.domain.model.AssetDomain;
import personal.finance.summary.domain.model.SummaryDomain;
import personal.finance.summary.output.Summary;
import personal.finance.summary.persistance.SummaryEntity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

class SummaryMapperTest {
    @Test
    void shouldMapSummaryDomainToEntity() {
        SummaryEntity actual = SummaryMapper.toEntity(SummaryDomain.builder()
                .id(1L)
                .moneyValue(BigDecimal.valueOf(200L))
                .state(SummaryState.CONFIRMED)
                .date(LocalDate.of(2022, 1, 1))
                .assets(List.of(
                        AssetDomain.builder()
                                .id(1L)
                                .moneyValue(BigDecimal.valueOf(200L))
                                .name("asset")
                                .items(List.of(ItemDomain.builder().id(1L).moneyValue(BigDecimal.valueOf(200L)).name("item").quantity(BigDecimal.valueOf(2)).build()))
                                .build()))
                .build());

        Assertions.assertThat(actual).usingRecursiveComparison()
                .ignoringFields("assets")
                .isEqualTo(SummaryEntity.builder()
                        .id(1L)
                        .moneyValue(BigDecimal.valueOf(200L))
                        .date(LocalDate.of(2022, 1, 1))
                        .state(SummaryState.CONFIRMED)
                        .build());
    }

    @Test
    void shouldMapSummaryToEntity() {
        SummaryEntity actual = SummaryMapper.toEntity(Summary.builder()
                .id(1L)
                .moneyValue(BigDecimal.valueOf(200).setScale(2, RoundingMode.HALF_UP))
                .state(SummaryState.CONFIRMED)
                .date(LocalDate.of(2022, 1, 1))
                .assets(List.of(
                        Asset.builder()
                                .id(1L)
                                .moneyValue(BigDecimal.valueOf(200).setScale(2, RoundingMode.HALF_UP))
                                .name("asset")
                                .items(List.of(Item.builder().id(1L).moneyValue(BigDecimal.valueOf(200L)).name("item").quantity(BigDecimal.valueOf(2)).build()))
                                .buildAsset()))
                .build());

        Assertions.assertThat(actual).usingRecursiveComparison()
                .isEqualTo(SummaryEntity.builder()
                        .id(1L)
                        .moneyValue(BigDecimal.valueOf(200).setScale(2, RoundingMode.HALF_UP))
                        .date(LocalDate.of(2022, 1, 1))
                        .state(SummaryState.CONFIRMED)
                        .assets(List.of(
                                Asset.builder()
                                        .id(1L)
                                        .moneyValue(BigDecimal.valueOf(200).setScale(2, RoundingMode.HALF_UP))
                                        .name("asset")
                                        .items(List.of(Item.builder().id(1L).moneyValue(BigDecimal.valueOf(200L)).name("item").quantity(BigDecimal.valueOf(2)).build()))
                                        .buildAsset()))
                        .build());
    }

    @Test
    void shouldMapToSummary() {
        SummaryEntity summaryEntity = SummaryEntity.builder()
                .id(1L)
                .moneyValue(BigDecimal.valueOf(200L))
                .date(LocalDate.of(2022, 1, 1))
                .state(SummaryState.CONFIRMED)
                .assets(List.of(
                        Asset.builder()
                                .id(1L)
                                .moneyValue(BigDecimal.valueOf(200L))
                                .name("asset")
                                .items(List.of(Item.builder().id(1L).moneyValue(BigDecimal.valueOf(200L)).name("item").quantity(BigDecimal.valueOf(2)).build()))
                                .buildAsset()))
                .build();
        Summary actual = SummaryMapper.toSummary(summaryEntity);

        Assertions.assertThat(actual)
                .isEqualTo(
                        Summary.builder()
                                .id(1L)
                                .moneyValue(BigDecimal.valueOf(200L))
                                .date(LocalDate.of(2022, 1, 1))
                                .state(SummaryState.CONFIRMED)
                                .assets(List.of(
                                        Asset.builder()
                                                .id(1L)
                                                .moneyValue(BigDecimal.valueOf(200L))
                                                .name("asset")
                                                .items(List.of(Item.builder().id(1L).moneyValue(BigDecimal.valueOf(200L)).name("item").quantity(BigDecimal.valueOf(2)).build()))
                                                .buildAsset()))
                                .build()
                );

    }
}
