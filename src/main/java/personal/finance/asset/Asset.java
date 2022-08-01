package personal.finance.asset;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;
import personal.finance.asset.item.Item;
import personal.finance.summary.persistance.SummaryEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author akazmierczak
 * @create 17.06.2022
 */
@Entity
@Builder(buildMethodName = "buildAsset")
@AllArgsConstructor
@Getter
@ToString(exclude = {"summary"})
public class Asset {

    public Asset() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private BigDecimal moneyValue;
    private String name;
    @OneToMany(mappedBy = "asset", cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private List<Item> items;

    @Setter
    @JsonIgnore
    @ManyToOne(optional = false)
    private SummaryEntity summary;

    public void addItem(Item item) {
        if (items == null) {
            items = new ArrayList<>();
        }
        item.setAsset(this);
        items.add(item);
    }

    public void addItems(List<Item> items) {
        items.forEach(
                this::addItem
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Asset asset = (Asset) o;
        return id != null && Objects.equals(id, asset.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
