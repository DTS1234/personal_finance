package personal.finance.summary.persistance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import personal.finance.asset.Asset;
import personal.finance.summary.SummaryState;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
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
public class SummaryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private BigDecimal moneyValue;
    private LocalDate date;
    private SummaryState state;

    @OneToMany(mappedBy = "summary", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Asset> assets;

    public void updateMoneyValue(BigDecimal newValue) {
        this.moneyValue = newValue;
    }

    public SummaryEntity() {

    }

    public void addAsset(Asset asset) {
        if (this.assets == null) {
            this.assets = new ArrayList<>();
        }
        this.assets.add(asset);
        asset.setSummary(this);
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
}
