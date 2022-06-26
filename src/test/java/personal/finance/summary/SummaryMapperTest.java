package personal.finance.summary;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import personal.finance.asset.Asset;
import personal.finance.asset.item.Item;
import personal.finance.asset.item.ItemDomain;
import personal.finance.summary.domain.model.AssetDomain;
import personal.finance.summary.domain.model.SummaryDomain;
import personal.finance.summary.persistance.SummaryEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SummaryMapperTest {
    @Test
    void shouldMapToEntity() {
        SummaryEntity actual = SummaryMapper.toEntity(SummaryDomain.builder()
                .id(1L)
                .moneyValue(BigDecimal.valueOf(200L))
                .state(SummaryState.CONFIRMED)
                .localDate(LocalDate.of(2022, 1, 1))
                .assets(List.of(
                        AssetDomain.builder()
                                .id(1L)
                                .moneyValue(BigDecimal.valueOf(200L))
                                .name("asset")
                                .items(List.of(ItemDomain.builder().id(1L).moneyValue(BigDecimal.valueOf(200L)).name("item").quantity(BigDecimal.valueOf(2)).build()))
                                .build()))
                .build());

        Assertions.assertThat(actual).usingRecursiveComparison()
                .isEqualTo(SummaryEntity.builder()
                        .id(1L)
                        .moneyValue(200L)
                        .date(LocalDate.of(2022, 1, 1))
                        .state(SummaryState.CONFIRMED)
                        .assets(List.of(
                                Asset.builder()
                                        .id(1L)
                                        .moneyValue(BigDecimal.valueOf(200L))
                                        .name("asset")
                                        .items(List.of(Item.builder().id(1L).moneyValue(BigDecimal.valueOf(200L)).name("item").quantity(BigDecimal.valueOf(2)).build()))
                                        .buildAsset()))
                        .build());
    }
}
