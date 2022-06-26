package acceptance;

import personal.finance.asset.Asset;
import personal.finance.asset.item.Item;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class GivenItems {
    public static List<List<Item>> itemLists(List<Asset> assets) {
        assert assets.size() == 3;
        Asset asset1 = assets.get(0);
        Asset asset2 = assets.get(1);
        Asset asset3 = assets.get(2);
        return Arrays.asList(
                Arrays.asList(
                        Item.builder().id(1L).moneyValue(BigDecimal.valueOf(500.21)).quantity(BigDecimal.valueOf(0.0000123)).name("Bitcoin").asset(asset1).build(),
                        Item.builder().id(2L).moneyValue(BigDecimal.valueOf(1000.22)).quantity(BigDecimal.valueOf(0.00543)).name("Ethereum").asset(asset1).build(),
                        Item.builder().id(3L).moneyValue(BigDecimal.valueOf(500.01)).quantity(BigDecimal.valueOf(100.00)).name("Luna").asset(asset1).build()
                ),
                List.of(Item.builder().moneyValue(BigDecimal.valueOf(2201.24)).name("ZL account").asset(asset2).quantity(BigDecimal.valueOf(1)).id(4L).build()),
                Arrays.asList(
                        Item.builder().id(5L).moneyValue(BigDecimal.valueOf(1201.12)).quantity(BigDecimal.valueOf(12)).name("CDP").asset(asset3).build(),
                        Item.builder().id(6L).moneyValue(BigDecimal.valueOf(1000.12)).quantity(BigDecimal.valueOf(43)).name("ALG").asset(asset3).build()
                )
        );
    }
}
