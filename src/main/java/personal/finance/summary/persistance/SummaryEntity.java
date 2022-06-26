package personal.finance.summary.persistance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import personal.finance.asset.Asset;
import personal.finance.summary.SummaryState;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.util.List;

/**
 * @author akazmierczak
 * @create 18.06.2022
 */
@Entity
@Builder
@Data
@AllArgsConstructor
public class SummaryEntity {

    @Id
    private Long id;
    private double moneyValue;
    private LocalDate date;
    private SummaryState state;

    @OneToMany(mappedBy = "summary", fetch = FetchType.EAGER)
    private List<Asset> assets;

    public SummaryEntity() {

    }

}
