package personal.finance.summary.domain;

import lombok.Value;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

import static personal.finance.summary.application.CurrencyManager.currencies;

@Value
public class Money {

    BigDecimal moneyValue;

    Currency currency;

    public Money(BigDecimal moneyValue) {
        this.moneyValue = moneyValue;
        this.currency = Currency.EUR;
    }

    public Money(BigDecimal moneyValue, Currency currency) {
        this.moneyValue = moneyValue;
        this.currency = currency;
    }

    public BigDecimal getMoneyValue() {
        if (currency != Currency.EUR) {
            return moneyValue.multiply(BigDecimal.valueOf(currencies.get(this.currency)));
        } else {
            return moneyValue;
        }
    }

    public Currency getCurrency() {
        if (this.currency == null) {
            return Currency.EUR;
        }
        return currency;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Money) {
            Money money = (Money) obj;
            return currency.equals(((Money) obj).currency) && moneyValue.setScale(2, RoundingMode.HALF_UP)
                .equals(money.moneyValue.setScale(2, RoundingMode.HALF_UP));
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(moneyValue);
    }
}
