package personal.finance.summary.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.Hibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author akazmierczak
 * @create 17.06.2022
 */
@Builder(buildMethodName = "buildAsset")
@AllArgsConstructor
@Getter
@ToString()
public class Asset {

    public Asset() {
    }

    private AssetId id;
    private Money money;
    private String name;
    private List<Item> items;

    public List<Item> getItems() {
        if (this.items == null) {
            return new ArrayList<>();
        }
        return this.items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Asset asset = (Asset) o;
        return id != null && Objects.equals(id, asset.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public void setMoney(Money newMoney) {
        this.money = newMoney;
    }
}
