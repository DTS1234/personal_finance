package personal.finance.summary.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Summary {

    private SummaryId id;
    private Long userId;
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
        this.money = new Money(sumAssetsMoneyValue());
    }

    public BigDecimal sumAssetsMoneyValue() {
        return this.assets.stream()
            .map(asset -> asset.getMoney().getMoneyValue())
            .reduce(BigDecimal.ZERO, BigDecimal::add)
            .setScale(2, RoundingMode.HALF_UP);
    }

    public boolean isInDraft() {
        return this.state.compareTo(SummaryState.DRAFT) != 0;
    }

    public BigDecimal sumOfItemsMoneyValue() {
        return getAssets().stream()
            .map(assetEntity -> assetEntity.getItems()
                .stream()
                .map(item -> item.getMoney().getMoneyValue()).reduce(BigDecimal.ZERO, BigDecimal::add))
            .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
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
}
