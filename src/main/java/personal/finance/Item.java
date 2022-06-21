package personal.finance;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * @author akazmierczak
 * @create 19.06.2022
 */
@Entity
@Builder
@Data
@AllArgsConstructor
public class Item {
    @Id
    private Long id;
    private BigDecimal moneyValue;
    private String name;
    @Column(precision = 20, scale = 7)
    @Convert(converter = BigDecimalConverterQuantity.class)
    private BigDecimal quantity;

    @ManyToOne
    @JoinColumn(name = "assetId")
    @JsonIgnore
    private Asset asset;

    public Item() {

    }

}
