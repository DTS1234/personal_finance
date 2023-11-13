package personal.finance.summary.domain;

import lombok.Value;

import java.math.BigDecimal;
import java.util.Objects;

@Value
public class Money {
    BigDecimal moneyValue;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Money) {
            Money money = (Money) obj;
            return  moneyValue.equals(money.moneyValue);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(moneyValue);
    }
}
