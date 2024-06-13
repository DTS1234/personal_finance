package personal.finance.tracking.asset.infrastracture.persistance.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Getter
@SuperBuilder
@Entity
@DiscriminatorValue("CUSTOM")
public class CustomItemEntity extends ItemEntity {

    public CustomItemEntity() {
        
    }
}
