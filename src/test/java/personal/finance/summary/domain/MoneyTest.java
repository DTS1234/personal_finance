package personal.finance.summary.domain;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(ReplaceUnderscores.class)
class MoneyTest {

    @Test
    void should_add_money_with_some_currencies() {
        // given
        Money m1 = new Money(12.15);
        Money m2 = new Money(100.15);
        // when
        Money result = m1.add(m2);
        // then
        assertThat(result).isEqualTo(new Money(112.30, Currency.EUR));
    }

    @Test
    void should_throw_if_currencies_are_different_for_add_ops() {
        Money m1 = new Money(12.15);
        Money m2 = new Money(100.15, Currency.USD);

        assertThatThrownBy(() -> m1.add(m2))
            .hasMessage(String.format("Currency mismatch: %s and %s", m1.getCurrency(), m2.getCurrency()));
    }

    @Test
    void should_subtract_money_with_some_currencies() {
        // given
        Money m1 = new Money(12.15);
        Money m2 = new Money(100.15);
        // when
        Money result = m1.subtract(m2);
        // then
        assertThat(result).isEqualTo(new Money(-88.00, Currency.EUR));
    }

    @Test
    void should_throw_if_currencies_are_different_for_substract_ops() {
        Money m1 = new Money(12.15);
        Money m2 = new Money(100.15, Currency.USD);

        assertThatThrownBy(() -> m1.subtract(m2))
            .hasMessage(String.format("Currency mismatch: %s and %s", m1.getCurrency(), m2.getCurrency()));
    }

    @Test
    void should_multiply_money() {
        // given
        Money m1 = new Money(5.50);
        // when
        Money result = m1.multiplyBy(3);
        // then
        assertThat(result).isEqualTo(new Money(16.50, Currency.EUR));
    }

    @Test
    void should_compare_money() {
        // given
        Money m1 = new Money(5.50);

        // then
        assertThat(m1.greaterThan(new Money(5.49))).isTrue();
        assertThat(m1.lessThan(new Money(5.51))).isTrue();
        assertThat(m1.lessOrEquals(new Money(5.50))).isTrue();
    }
}
