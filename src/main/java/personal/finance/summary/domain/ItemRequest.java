package personal.finance.summary.domain;

import java.math.BigDecimal;

public record ItemRequest(String name, BigDecimal quantity, Money money) {

}
