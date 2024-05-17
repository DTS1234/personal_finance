package personal.finance.tracking.summary.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
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

    public void updateMoneyValue(Money newValue) {
        this.money = newValue;
    }

    public boolean isInDraft() {
        return this.state.compareTo(SummaryState.DRAFT) != 0;
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

    public UUID getIdValue() {
        return this.id.getValue();
    }
}
