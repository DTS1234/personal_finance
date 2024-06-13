package personal.finance.tracking.asset.application;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@NoArgsConstructor
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    property = "type"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = StockItemDTO.class, name = "STOCK"),
    @JsonSubTypes.Type(value = CustomItemDTO.class, name = "CUSTOM")
})
public abstract class ItemDTO {

    UUID id;
    BigDecimal money;
    String name;

    public ItemDTO(UUID id, BigDecimal money, String name) {
        this.id = id;
        this.money = money;
        this.name = name;
    }
}
