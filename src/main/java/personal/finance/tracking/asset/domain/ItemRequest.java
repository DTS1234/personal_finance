package personal.finance.tracking.asset.domain;

import personal.finance.tracking.summary.domain.Money;

import java.math.BigDecimal;

public record ItemRequest(String name, BigDecimal quantity, Money money) {

}
