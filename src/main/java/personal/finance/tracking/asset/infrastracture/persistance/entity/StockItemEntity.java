package personal.finance.tracking.asset.infrastracture.persistance.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Getter
@SuperBuilder
@Entity
@DiscriminatorValue("STOCK")
public class StockItemEntity extends ItemEntity {

    private String ticker;
    private BigDecimal purchasePrice;
    private BigDecimal currentPrice;
    private BigDecimal quantity;

    public StockItemEntity() {

    }
}
