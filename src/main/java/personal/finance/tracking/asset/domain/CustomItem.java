package personal.finance.tracking.asset.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;
import personal.finance.tracking.summary.domain.Money;

import java.util.Objects;

/**
 * @author akazmierczak
 * @create 19.06.2022
 */
@SuperBuilder
@ToString()
@Getter
@Setter
public class CustomItem extends Item {

    public CustomItem(ItemId id, Money money, String name) {
        super(id, money, name);
    }

    @Override
    public CustomItem newCopyForAsset() {
        return new CustomItem(ItemId.random(), this.money, this.name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        CustomItem item = (CustomItem) o;
        return id != null && Objects.equals(id, item.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
