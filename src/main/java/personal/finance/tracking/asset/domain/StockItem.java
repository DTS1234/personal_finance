package personal.finance.tracking.asset.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.lang.NonNull;
import personal.finance.tracking.summary.domain.Money;

import java.math.BigDecimal;

@Getter
@Setter
@SuperBuilder
public class StockItem extends Item {

    @NonNull
    private String ticker;

    @NonNull
    private Money purchasePrice;

    @NonNull
    private Money currentPrice;

    @NonNull
    private BigDecimal quantity;

    public StockItem(ItemId id, Money money, String name) {
        super(id, money, name);
    }

    public StockItem() {
        super(ItemId.random(), new Money(0), null);
    }

    public Money calculateProfit() {
        Money profitPerUnit = currentPrice.subtract(purchasePrice);
        return profitPerUnit.multiplyBy(quantity);
    }

    public double calculateProfitPercentage() {
        Money subtract = currentPrice.subtract(purchasePrice);
        Money money = subtract.divideBy(purchasePrice.getMoneyValue());
        return money.multiplyBy(100).getMoneyValue().doubleValue();
    }

    public StockItem buyMore(BigDecimal quantity, Money purchasePrice) {
        Money currentCost = this.purchasePrice.multiplyBy(this.quantity);
        Money newCost = purchasePrice.multiplyBy(quantity);
        Money totalCost = currentCost.add(newCost);
        BigDecimal totalQuantity = quantity.add(this.quantity);

        Money adjustedPurchasePrice = totalCost.divideBy(totalQuantity);
        this.setPurchasePrice(adjustedPurchasePrice);
        this.quantity = totalQuantity;
        this.setMoney(this.currentPrice.multiplyBy(totalQuantity));

        return this;
    }

    @Override
    public Item newCopyForAsset() {
        return StockItem.builder()
            .id(ItemId.random())
            .quantity(this.quantity)
            .ticker(this.ticker)
            .purchasePrice(this.purchasePrice)
            .currentPrice(this.currentPrice)
            .money(this.money)
            .name(this.name)
            .build();
    }
}
