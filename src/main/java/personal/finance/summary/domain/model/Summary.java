package personal.finance.summary.domain.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import personal.finance.summary.SummaryState;

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
public class Summary {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter
    private Long id;
    private BigDecimal moneyValue;
    private LocalDateTime date;
    private SummaryState state;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Asset> assets;

    public void updateMoneyValue(BigDecimal newValue) {
        this.moneyValue = newValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Summary that = (Summary) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public void addAsset(Asset asset) {
        if (this.assets == null) {
            this.assets = new ArrayList<>();
        }
        this.assets.add(asset);
        this.moneyValue = sumAssetsMoneyValue();
    }

    public BigDecimal sumAssetsMoneyValue() {
        return this.assets.stream().map(Asset::getMoneyValue).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public boolean isInDraft() {
        return this.state.compareTo(SummaryState.DRAFT) == 0;
    }

    public BigDecimal sumOfItemsMoneyValue() {
        return getAssets().stream()
            .map(assetEntity -> assetEntity.getItems().stream().map(Item::getMoneyValue).reduce(BigDecimal.ZERO, BigDecimal::add))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Summary confirm() {
        this.state = SummaryState.CONFIRMED;
        return this;
    }

    public boolean isCancelled() {
        return this.state.compareTo(SummaryState.CANCELED) == 0;
    }

    public Summary cancel() {
        this.state = SummaryState.CANCELED;
        return this;
    }
}
