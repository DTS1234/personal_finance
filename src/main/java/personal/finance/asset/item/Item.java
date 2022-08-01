package personal.finance.asset.item;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;
import personal.finance.asset.Asset;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * @author akazmierczak
 * @create 19.06.2022
 */
@Entity
@Builder
@ToString(exclude = "asset")
@Getter
@AllArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private BigDecimal moneyValue;
    private String name;
    @Column(precision = 20, scale = 7)
    @Convert(converter = BigDecimalConverterQuantity.class)
    private BigDecimal quantity;

    @ManyToOne(optional = false)
    @JsonIgnore
    @Setter
    private Asset asset;

    public Item() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Item item = (Item) o;
        return id != null && Objects.equals(id, item.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
