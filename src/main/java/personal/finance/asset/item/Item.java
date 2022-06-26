package personal.finance.asset.item;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import personal.finance.asset.Asset;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * @author akazmierczak
 * @create 19.06.2022
 */
@Entity
@Builder
@ToString(exclude = "asset")
@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private BigDecimal moneyValue;
    private String name;
    @Column(precision = 20, scale = 7)
    @Convert(converter = BigDecimalConverterQuantity.class)
    private BigDecimal quantity;

    @ManyToOne(optional = false)
    @JsonIgnore
    @Setter
    private Asset asset;

    public Item() {

    }

}
