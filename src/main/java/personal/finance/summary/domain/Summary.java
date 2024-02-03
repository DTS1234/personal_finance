package personal.finance.summary.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Summary {

    private SummaryId id;
    private UUID userId;
    private Money money;
    private LocalDateTime date;
    private SummaryState state;
    private List<Asset> assets;

    public void updateMoneyValue(Money newValue) {
        this.money = newValue;
    }

    public void addAsset(Asset asset) {
        if (this.assets == null) {
            this.assets = new ArrayList<>();
        }
        this.assets.add(asset);
        this.money = sumAssetsMoney();
    }

    public Money sumAssetsMoney() {
        return this.assets.stream()
            .map(Asset::getMoney)
            .reduce(new Money(0), Money::add);
    }

    public boolean isInDraft() {
        return this.state.compareTo(SummaryState.DRAFT) != 0;
    }

    public Money sumItemsMoney() {
        return getAssets().stream()
            .map(assetEntity -> assetEntity.getItems()
                .stream()
                .map(Item::getMoney)
                .reduce(new Money(0), Money::add))
            .reduce(new Money(0), Money::add);
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

    public List<Asset> getAssets() {
        if (this.assets == null) {
            return new ArrayList<>();
        }
        return this.assets;
    }

    public UUID getIdValue() {
        return this.id.getValue();
    }
}
