package personal.finance;

import personal.finance.tracking.asset.domain.Asset;
import personal.finance.tracking.asset.domain.AssetId;
import personal.finance.tracking.asset.domain.AssetType;
import personal.finance.tracking.asset.domain.CustomItem;
import personal.finance.tracking.asset.domain.ItemId;
import personal.finance.tracking.asset.domain.StockItem;
import personal.finance.tracking.summary.domain.Money;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author akazmierczak
 * @create 19.06.2022
 */
public class GivenAssets {

    public static List<Asset> assets() {
        AssetId assetIdOne = AssetId.random();
        AssetId assetIdTwo = AssetId.random();
        AssetId assetIdThree = AssetId.random();

        return Arrays.asList(
            Asset.builder()
                .id(assetIdOne)
                .name("Crypto")
                .money(new Money(BigDecimal.valueOf(500.31)))
                .items(List.of(
                    StockItem.builder().id(ItemId.random()).money(new Money(BigDecimal.valueOf(200.00)))
                        .build(),
                    StockItem.builder().id(ItemId.random()).money(new Money(BigDecimal.valueOf(300.31)))
                        .build()
                ))
                .type(AssetType.CUSTOM)
                .buildAsset(),
            Asset.builder()
                .id(assetIdTwo)
                .name("Account 1")
                .money(new Money(BigDecimal.valueOf(1500.12)))
                .items(List.of(
                    CustomItem.builder().id(ItemId.random()).money(new Money(BigDecimal.valueOf(200.00)))
                        .build(),
                    CustomItem.builder().id(ItemId.random()).money(new Money(BigDecimal.valueOf(300.01)))
                        .build(),
                    CustomItem.builder().id(ItemId.random()).money(new Money(BigDecimal.valueOf(1000.11)))
                        .build()
                ))
                .type(AssetType.CUSTOM)
                .buildAsset(),
            Asset.builder()
                .id(assetIdThree)
                .name("Stocks 1")
                .money(new Money(BigDecimal.valueOf(2201.24)))
                .items(List.of(
                    StockItem.builder().id(ItemId.random()).money(new Money(BigDecimal.valueOf(201.20)))
                        .build(),
                    StockItem.builder().id(ItemId.random()).money(new Money(BigDecimal.valueOf(2000.04)))
                        .build()
                ))
                .type(AssetType.CUSTOM)
                .buildAsset());
        // sum of values : 2000.43 + 2201.24 = 4201.67
        // percentages: 11,91 %, 35,70%, 52,39%
    }

    public static Asset newAssetOfTenWithOneItem() {
        CustomItem testItemOne = CustomItem.builder()
            .money(new Money(BigDecimal.valueOf(10, 0)))
            .name("testItemOne")
            .id(ItemId.random())
            .build();

        return Asset.builder()
            .id(AssetId.random())
            .money(new Money(BigDecimal.valueOf(10, 0)))
            .items(new ArrayList<>(
                List.of(testItemOne)
            ))
            .type(AssetType.CUSTOM)
            .buildAsset();
    }

}
