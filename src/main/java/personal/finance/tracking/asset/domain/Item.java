package personal.finance.tracking.asset.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import personal.finance.tracking.summary.domain.Money;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * @author akazmierczak
 * @create 19.06.2022
 */
@Builder
@ToString()
@Getter
@Setter
@AllArgsConstructor
public class Item {

    private ItemId id;
    private Money money;
    private String name;
    private BigDecimal quantity;

    public Item() {

    }

    public UUID getIdValue() {
        return id.getValue();
    }

    public Item newCopyForAsset() {
        return new Item(ItemId.random(), this.money, this.name, this.quantity);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Item item = (Item) o;
        return id != null && Objects.equals(id, item.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
