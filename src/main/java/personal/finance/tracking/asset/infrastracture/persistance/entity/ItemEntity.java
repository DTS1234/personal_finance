package personal.finance.tracking.asset.infrastracture.persistance.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.Hibernate;
import personal.finance.common.BigDecimalConverterQuantity;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * @author akazmierczak
 * @create 19.06.2022
 */
@Entity
@Builder
@ToString()
@Getter
@AllArgsConstructor
public class ItemEntity {

    @Id
    private UUID id;
    private BigDecimal moneyValue;
    private String name;
    @Column(precision = 20, scale = 7)
    @Convert(converter = BigDecimalConverterQuantity.class)
    private BigDecimal quantity;

    public ItemEntity() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        ItemEntity itemEntity = (ItemEntity) o;
        return id != null && Objects.equals(id, itemEntity.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
