package personal.finance.tracking.summary.infrastracture.persistance.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import personal.finance.tracking.summary.domain.SummaryState;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
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
