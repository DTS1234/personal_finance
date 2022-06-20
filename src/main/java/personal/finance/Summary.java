package personal.finance;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;

/**
 * @author akazmierczak
 * @create 18.06.2022
 */
@Entity
@Builder
@Data
public class Summary {

    @Id
    private Long id;
    private double moneyValue;
    private LocalDate date;

    public Summary() {

    }

    public Summary(Long id, double moneyValue, LocalDate date) {
        this.id = id;
        this.moneyValue = moneyValue;
        this.date = date;
    }
}
