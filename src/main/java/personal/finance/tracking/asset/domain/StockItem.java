package personal.finance.tracking.asset.domain;

import lombok.Getter;
import lombok.Setter;
import personal.finance.tracking.summary.domain.Money;

import java.math.BigDecimal;

public class StockItem extends Item {

    @Setter
    private String ticker;

    @Getter
    @Setter
    private Money purchasePrice;

    @Setter
    @Getter
    private Money currentPrice;

    public Money calculateProfit() {
        Money profitPerUnit = currentPrice.subtract(purchasePrice);
        return profitPerUnit.multiplyBy(this.getQuantity());
    }

    public double calculateProfitPercentage() {
        Money subtract = currentPrice.subtract(purchasePrice);
        Money money = subtract.divideBy(purchasePrice.getMoneyValue());
        return money.multiplyBy(100).getMoneyValue().doubleValue();
    }

    public StockItem buyMore(BigDecimal quantity, Money purchasePrice) {
        Money currentCost = this.purchasePrice.multiplyBy(this.getQuantity());
        Money newCost = purchasePrice.multiplyBy(quantity);
        Money totalCost = currentCost.add(newCost);
        BigDecimal totalQuantity = quantity.add(this.getQuantity());

        Money adjustedPurchasePrice = totalCost.divideBy(totalQuantity);
        this.setPurchasePrice(adjustedPurchasePrice);
        this.setQuantity(totalQuantity);
        this.setMoney(this.currentPrice.multiplyBy(totalQuantity));

        return this;
    }
}
