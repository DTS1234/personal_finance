package personal.finance.summary;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import personal.finance.asset.AssetEntity;
import personal.finance.item.Item;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author akazmierczak
 * @create 18.06.2022
 */
@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SummaryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter
    private Long id;
    private BigDecimal moneyValue;
    private LocalDateTime date;
    private SummaryState state;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<AssetEntity> assets;

    public void updateMoneyValue(BigDecimal newValue) {
        this.moneyValue = newValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SummaryEntity that = (SummaryEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public void addAsset(AssetEntity asset) {
        if (this.assets == null){
            this.assets = new ArrayList<>();
        }
        this.assets.add(asset);
        this.moneyValue = sumAssetsMoneyValue();
    }

    public BigDecimal sumAssetsMoneyValue() {
        return this.assets.stream().map(AssetEntity::getMoneyValue).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public boolean isInDraft() {
        return this.state.compareTo(SummaryState.DRAFT) == 0;
    }
    public BigDecimal sumOfItemsMoneyValue() {
        return getAssets().stream()
            .map(assetEntity -> assetEntity.getItems().stream().map(Item::getMoneyValue).reduce(BigDecimal.ZERO, BigDecimal::add))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
