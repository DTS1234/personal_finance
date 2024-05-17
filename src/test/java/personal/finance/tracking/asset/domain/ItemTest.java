package personal.finance.tracking.asset.domain;

import org.junit.jupiter.api.Test;
import personal.finance.tracking.summary.domain.Money;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class ItemTest {

    @Test
    void should_calculate_the_income_on_item() {
        // given
        StockItem item = new StockItem();
        item.setTicker("TSLA");
        item.setQuantity(BigDecimal.TWO);
        item.setPurchasePrice(new Money(160.50));
        item.setCurrentPrice(new Money(170.00));

        // when
        Money money = item.calculateProfit();

        // then
        assertThat(money).isEqualTo(new Money(19.00));
    }

    @Test
    void should_calculate_the_income_percentage_on_item() {
        // given
        StockItem item = new StockItem();
        item.setTicker("TSLA");
        item.setQuantity(BigDecimal.TWO);
        item.setPurchasePrice(new Money(160.50));
        item.setCurrentPrice(new Money(170.00));

        // when
        double percentage = item.calculateProfitPercentage();

        // then
        assertThat(percentage).isEqualTo(5.92);
    }

    @Test
    void should_adjust_parameters_when_new_stock_is_bought() {
        // given
        StockItem item = new StockItem();
        item.setTicker("TSLA");
        item.setQuantity(BigDecimal.TWO);
        item.setPurchasePrice(new Money(160.50));
        item.setCurrentPrice(new Money(170.50));
        item.setMoney(item.getCurrentPrice().multiplyBy(item.getQuantity()));

        // when
        StockItem newStockItem = item.buyMore(BigDecimal.valueOf(3), new Money(150.50));

        // then
        assertThat(newStockItem.getPurchasePrice()).isEqualTo(new Money(154.50));
        assertThat(newStockItem.calculateProfit()).isEqualTo(new Money(80.00));
        assertThat(newStockItem.calculateProfitPercentage()).isEqualTo(10.36);
        assertThat(newStockItem.getMoney()).isEqualTo(new Money(852.50));
    }
}
