package personal.finance.tracking.summary.domain;

import lombok.Value;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Value
public class Money {

    @NonNull
    BigDecimal moneyValue;

    Currency currency;

    public Money(@NonNull BigDecimal moneyValue) {
        this.moneyValue = moneyValue.setScale(2, RoundingMode.HALF_EVEN);
        this.currency = Currency.EUR;
    }

    public Money(BigDecimal moneyValue, Currency currency) {
        this.moneyValue = moneyValue.setScale(4, RoundingMode.HALF_EVEN);
        this.currency = currency;
    }

    public Money(double money) {
        this.moneyValue = BigDecimal.valueOf(money);
        this.currency = Currency.EUR;
    }

    public Money(double money, Currency currency) {
        this.moneyValue = BigDecimal.valueOf(money);
        this.currency = currency;
    }

    public Currency getCurrency() {
        if (this.currency == null) {
            return Currency.EUR;
        }
        return currency;
    }

    public BigDecimal getMoneyValue() {
        return this.moneyValue.setScale(2, RoundingMode.HALF_EVEN);
    }

    public Money add(Money other) {
        if (currencyCompatible(other)) {
            throw new IllegalArgumentException(
                String.format("Currency mismatch: %s and %s", this.getCurrency(), other.getCurrency()));
        }

        return new Money(moneyValue.add(other.moneyValue).setScale(2, RoundingMode.HALF_UP),
            determineCurrencyCode(other));
    }

    public Money subtract(Money other) {
        if (currencyCompatible(other)) {
            throw new IllegalArgumentException(
                String.format("Currency mismatch: %s and %s", this.getCurrency(), other.getCurrency()));
        }

        return new Money(moneyValue.subtract(other.moneyValue), determineCurrencyCode(other));
    }

    public Money multiplyBy(double multiplier) {
        return multiplyBy(new BigDecimal(multiplier));
    }

    public Money multiplyBy(BigDecimal multiplier) {
        return new Money(moneyValue.multiply(multiplier), currency);
    }

    public Money divideBy(BigDecimal divisor) {
        return new Money(moneyValue.divide(divisor, 4, RoundingMode.HALF_EVEN), currency);
    }

    public Money divideBy(double divisor) {
        return new Money(moneyValue.divide(BigDecimal.valueOf(divisor), RoundingMode.HALF_UP), currency);
    }

    public boolean greaterThan(Money other) {
        return moneyValue.compareTo(other.moneyValue) > 0;
    }

    public boolean lessThan(Money other) {
        return moneyValue.compareTo(other.moneyValue) < 0;
    }

    public boolean lessOrEquals(Money other) {
        return moneyValue.compareTo(other.moneyValue) <= 0;
    }

    private boolean currencyCompatible(Money money) {
        return !isZero(this.moneyValue) && !isZero(money.moneyValue) && currency != (money.getCurrency());
    }

    private Currency determineCurrencyCode(Money otherMoney) {
        return isZero(this.moneyValue) ? otherMoney.currency : this.currency;
    }

    private boolean isZero(BigDecimal testedValue) {
        return BigDecimal.ZERO.compareTo(testedValue) == 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Money money) {
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
