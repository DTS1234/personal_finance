package personal.finance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author akazmierczak
 * @create 17.06.2022
 */
@Entity
@Builder(buildMethodName = "buildAsset")
@Data
@AllArgsConstructor
public class Asset {

    public Asset() {
    }

    @Id
    private Long id;
    private BigDecimal moneyValue;
    private String name;
    @OneToMany(mappedBy = "asset")
    private List<Item> items;

}
