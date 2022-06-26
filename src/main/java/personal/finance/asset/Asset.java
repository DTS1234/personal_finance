package personal.finance.asset;

import lombok.*;
import personal.finance.asset.item.Item;
import personal.finance.summary.persistance.SummaryEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author akazmierczak
 * @create 17.06.2022
 */
@Entity
@Builder(buildMethodName = "buildAsset")
@AllArgsConstructor
@Getter
@ToString( exclude = {"summary"})
public class Asset {

    public Asset() {
    }

    @Id
    private Long id;
    private BigDecimal moneyValue;
    private String name;
    @OneToMany(mappedBy = "asset", cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private List<Item> items;
    @ManyToOne
    @JoinColumn(name = "summaryId")
    @Setter
    private SummaryEntity summary;

    public void addItem(Item item) {
        if(items == null) {
            items = new ArrayList<>();
        }
        item.setAsset(this);
        items.add(item);
    }

    public void addItems(List<Item> items) {
        items.forEach(
                this::addItem
        );
    }
}
