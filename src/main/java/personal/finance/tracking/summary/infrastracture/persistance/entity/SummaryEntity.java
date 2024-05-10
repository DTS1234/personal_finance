package personal.finance.tracking.summary.infrastracture.persistance.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import personal.finance.tracking.asset.infrastracture.persistance.entity.AssetEntity;
import personal.finance.tracking.summary.domain.SummaryState;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SummaryEntity {

    @Id
    @Setter
    private UUID id;

    @Column(nullable = false)
    private UUID userId;
    private BigDecimal moneyValue;
    private LocalDateTime date;
    private SummaryState state;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<AssetEntity> assetEntities;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        SummaryEntity that = (SummaryEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public void addAsset(AssetEntity assetEntity) {
        if (this.assetEntities == null) {
            this.assetEntities = new ArrayList<>();
        }
        this.assetEntities.add(assetEntity);
        this.moneyValue = sumAssetsMoneyValue();
    }

    public BigDecimal sumAssetsMoneyValue() {
        return this.assetEntities.stream().map(AssetEntity::getMoneyValue).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public SummaryEntity confirm() {
        this.state = SummaryState.CONFIRMED;
        return this;
    }

    public boolean isCancelled() {
        return this.state.compareTo(SummaryState.CANCELED) == 0;
    }

    public SummaryEntity cancel() {
        this.state = SummaryState.CANCELED;
        return this;
    }
}
