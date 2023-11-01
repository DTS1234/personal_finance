package acceptance;

import personal.finance.summary.domain.model.Item;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class GivenItems {
    public static List<List<Item>> itemLists() {
        return Arrays.asList(
                Arrays.asList(
                        Item.builder().id(1L).moneyValue(BigDecimal.valueOf(500.21)).quantity(BigDecimal.valueOf(0.0000123)).name("Bitcoin").build(),
                        Item.builder().id(2L).moneyValue(BigDecimal.valueOf(1000.22)).quantity(BigDecimal.valueOf(0.00543)).name("Ethereum").build(),
                        Item.builder().id(3L).moneyValue(BigDecimal.valueOf(500.01)).quantity(BigDecimal.valueOf(100.00)).name("Luna").build()
                ),
                List.of(Item.builder().moneyValue(BigDecimal.valueOf(2201.24)).name("ZL account").quantity(BigDecimal.valueOf(1)).id(4L).build()),
                Arrays.asList(
                        Item.builder().id(5L).moneyValue(BigDecimal.valueOf(1201.12)).quantity(BigDecimal.valueOf(12)).name("CDP").build(),
                        Item.builder().id(6L).moneyValue(BigDecimal.valueOf(1000.12)).quantity(BigDecimal.valueOf(43)).name("ALG").build()
                )
        );
    }

    public static List<Item> itemList() {
        return Arrays.asList(
            Item.builder().id(1L).moneyValue(BigDecimal.valueOf(500.21)).quantity(BigDecimal.valueOf(0.0000123)).name("Bitcoin").build(),
            Item.builder().id(2L).moneyValue(BigDecimal.valueOf(1000.22)).quantity(BigDecimal.valueOf(0.00543)).name("Ethereum").build(),
            Item.builder().id(3L).moneyValue(BigDecimal.valueOf(500.01)).quantity(BigDecimal.valueOf(100.00)).name("Luna").build()
        );
    }

}
