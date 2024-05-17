package personal.finance.tracking.asset.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import personal.finance.tracking.summary.domain.Money;
import personal.finance.tracking.summary.domain.SummaryId;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

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

    @Setter
    private AssetId id;
    @Setter
    private Money money;
    private String name;
    private List<Item> items;
    private AssetType type;
    @Setter
    private SummaryId summaryId;

    public Asset(AssetId id, Money money, String name, List<Item> items, SummaryId summaryId) {
        this.id = id;
        this.money = money;
        this.name = name;
        this.items = items;
        this.type = AssetType.NORMAL;
        this.summaryId = summaryId;
    }

    public List<Item> getItems() {
        if (this.items == null) {
            return new ArrayList<>();
        }
        return this.items;
    }

    public Asset newCopyForSummary(SummaryId newSummaryId) {
        return new Asset(AssetId.random(), this.money, this.name, this.items.stream().map(Item::newCopyForAsset).toList(), newSummaryId);
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

    public UUID getIdValue() {
        return this.id.getValue();
    }

    public UUID getSummaryIdValue() {
        return Optional.ofNullable(this.summaryId).map(SummaryId::getValue).orElse(null);
    }
}
