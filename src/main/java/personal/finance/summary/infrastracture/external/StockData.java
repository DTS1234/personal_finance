package personal.finance.summary.infrastracture.external;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@NoArgsConstructor
public class StockData {

    @Getter
    @Id
    @Column(name = "id", nullable = false)
    @JsonIgnore
    private UUID id;

    public LocalDate date;
    public BigDecimal open;
    public BigDecimal high;
    public BigDecimal low;
    public BigDecimal close;
    public BigDecimal adjusted_close;
    public BigDecimal volume;

    public StockData(LocalDate date, BigDecimal open, BigDecimal high, BigDecimal low, BigDecimal close,
        BigDecimal adjusted_close, BigDecimal volume) {
        this.id = UUID.randomUUID();
        this.date = date;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.adjusted_close = adjusted_close;
        this.volume = volume;
    }
}
