package personal.finance;

import personal.finance.summary.domain.Asset;
import personal.finance.summary.domain.AssetId;
import personal.finance.summary.domain.Item;
import personal.finance.summary.domain.ItemId;
import personal.finance.summary.domain.Money;

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
        return Arrays.asList(
            Asset.builder()
                .id(new AssetId(1L))
                .name("Crypto")
                .money(new Money(BigDecimal.valueOf(500.31)))
                .items(List.of(
                    Item.builder().money(new Money(BigDecimal.valueOf(200.00))).build(),
                    Item.builder().money(new Money(BigDecimal.valueOf(300.31))).build()
                ))
                .buildAsset(),
            Asset.builder()
                .id(new AssetId(2L))
                .name("Account 1")
                .money(new Money(BigDecimal.valueOf(1500.12)))
                .items(List.of(
                    Item.builder().money(new Money(BigDecimal.valueOf(200.00))).build(),
                    Item.builder().money(new Money(BigDecimal.valueOf(300.01))).build(),
                    Item.builder().money(new Money(BigDecimal.valueOf(1000.11))).build()
                ))
                .buildAsset(),
            Asset.builder()
                .id(new AssetId(3L))
                .name("Stocks 1")
                .money(new Money(BigDecimal.valueOf(2201.24)))
                .items(List.of(
                    Item.builder().money(new Money(BigDecimal.valueOf(201.20))).build(),
                    Item.builder().money(new Money(BigDecimal.valueOf(2000.04))).build()
                ))
                .buildAsset());
        // sum of values : 2000.43 + 2201.24 = 4201.67
        // percentages: 11,91 %, 35,70%, 52,39%
    }

    public static Asset newAssetOfTenWithOneItem() {
        Item testItemOne = Item.builder()
            .money(new Money(BigDecimal.valueOf(10, 0)))
            .name("testItemOne")
            .id(new ItemId(1L))
            .quantity(BigDecimal.valueOf(1L))
            .build();

        return Asset.builder()
            .id(new AssetId(1L))
            .money(new Money(BigDecimal.valueOf(10, 0)))
            .items(new ArrayList<>(
                List.of(testItemOne)
            )).buildAsset();
    }

}
