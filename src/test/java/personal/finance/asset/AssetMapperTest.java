package personal.finance.asset;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import personal.finance.asset.item.Item;
import personal.finance.asset.item.ItemDomain;
import personal.finance.summary.domain.model.AssetDomain;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AssetMapperTest {

    @Test
    void shouldMapToEntity() {
        AssetDomain assetDomain = AssetDomain.builder()
                .id(1L)
                .name("asset")
                .moneyValue(BigDecimal.valueOf(200L))
                .items(List.of(ItemDomain.builder().id(1L).moneyValue(BigDecimal.valueOf(200L)).name("item").quantity(BigDecimal.valueOf(2)).build()))
                .build();

        Asset asset = AssetMapper.toEntity(assetDomain);

        Assertions.assertThat(asset)
                .usingRecursiveComparison().isEqualTo(Asset.builder()
                        .id(1L)
                        .moneyValue(BigDecimal.valueOf(200L))
                        .name("asset")
                        .items(List.of(Item.builder().id(1L).moneyValue(BigDecimal.valueOf(200L)).name("item").quantity(BigDecimal.valueOf(2)).build()))
                        .buildAsset());
    }
}
