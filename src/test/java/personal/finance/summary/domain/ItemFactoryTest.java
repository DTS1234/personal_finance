package personal.finance.summary.domain;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import personal.finance.tracking.asset.domain.Item;
import personal.finance.tracking.asset.domain.ItemFactory;
import personal.finance.tracking.asset.domain.ItemRequest;
import personal.finance.tracking.asset.domain.StockItem;
import personal.finance.tracking.asset.domain.StockItemRequest;
import personal.finance.tracking.summary.domain.Currency;
import personal.finance.tracking.summary.domain.Money;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(ReplaceUnderscores.class)
class ItemFactoryTest {

    @Test
    void should_create_a_regular_item() {
        ItemFactory itemFactory = new ItemFactory();
        Item item = itemFactory.createItem("Normal", new ItemRequest("Cash account", null, new Money(1000.00)));

        assertThat(item.getMoney()).isEqualTo(new Money(1000.00, Currency.EUR));
        assertThat(item.getName()).isEqualTo("Cash account");
        assertThat(item.getId()).isNotNull();
        assertThat(item.getQuantity()).isNull();
    }

    @Test
    void should_create_a_stock_item() {
        ItemFactory itemFactory = new ItemFactory();
        Item item = itemFactory.createItem("STOCK", new StockItemRequest("AAPL", BigDecimal.TEN, BigDecimal.TWO, "Apple Stock", BigDecimal.ONE));
        StockItem casted = (StockItem)item;

        assertThat(casted.getMoney()).isEqualTo(new Money(2.00, Currency.EUR));
        assertThat(casted.getName()).isEqualTo("Apple Stock");
        assertThat(casted.getId()).isNotNull();
        assertThat(casted.getQuantity()).isEqualTo(BigDecimal.ONE);
        assertThat(casted.getCurrentPrice()).isEqualTo(new Money(BigDecimal.TWO));
        assertThat(casted.getPurchasePrice()).isEqualTo(new Money(BigDecimal.TEN));
        assertThat(casted.calculateProfit()).isEqualTo(new Money(-8.00));
    }
}