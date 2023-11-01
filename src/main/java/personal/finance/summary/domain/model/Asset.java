package personal.finance.summary.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.Hibernate;

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
@ToString()
public class Asset {

    public Asset() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private BigDecimal moneyValue;
    private String name;
    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private List<Item> items;

    public void addItem(Item item) {
        if (items == null) {
            items = new ArrayList<>();
        }
        items.add(item);
        BigDecimal sum = items.stream().map(Item::getMoneyValue).reduce(BigDecimal.ZERO, BigDecimal::add);
        this.moneyValue = sum;
    }

    public void addItems(List<Item> items) {
        items.forEach(
                this::addItem
        );
    }

    public void clearItems() {
        this.items.clear();
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
