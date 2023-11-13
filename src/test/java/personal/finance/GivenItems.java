package personal.finance;

import personal.finance.summary.domain.Item;
import personal.finance.summary.domain.ItemId;
import personal.finance.summary.domain.Money;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class GivenItems {
    public static List<List<Item>> itemLists() {
        return Arrays.asList(
                Arrays.asList(
                        Item.builder().id(new ItemId(1L)).money(new Money(BigDecimal.valueOf(500.21))).quantity(BigDecimal.valueOf(0.0000123)).name("Bitcoin").build(),
                        Item.builder().id(new ItemId(2L)).money(new Money(BigDecimal.valueOf(1000.22))).quantity(BigDecimal.valueOf(0.00543)).name("Ethereum").build(),
                        Item.builder().id(new ItemId(3L)).money(new Money(BigDecimal.valueOf(500.01))).quantity(BigDecimal.valueOf(100.00)).name("Luna").build()
                ),
                List.of(Item.builder().money(new Money(BigDecimal.valueOf(2201.24))).name("ZL account").quantity(BigDecimal.valueOf(1)).id(new ItemId(4L)).build()),
                Arrays.asList(
                        Item.builder().id(new ItemId(5L)).money(new Money(BigDecimal.valueOf(1201.12))).quantity(BigDecimal.valueOf(12)).name("CDP").build(),
                        Item.builder().id(new ItemId(6L)).money(new Money(BigDecimal.valueOf(1000.12))).quantity(BigDecimal.valueOf(43)).name("ALG").build()
                )
        );
    }

    public static List<Item> itemList() {
        return Arrays.asList(
            Item.builder().id(new ItemId(1L)).money(new Money(BigDecimal.valueOf(500.21))).quantity(BigDecimal.valueOf(0.0000123)).name("Bitcoin").build(),
            Item.builder().id(new ItemId(2L)).money(new Money(BigDecimal.valueOf(1000.22))).quantity(BigDecimal.valueOf(0.00543)).name("Ethereum").build(),
            Item.builder().id(new ItemId(3L)).money(new Money(BigDecimal.valueOf(500.01))).quantity(BigDecimal.valueOf(100.00)).name("Luna").build()
        );
    }

}
