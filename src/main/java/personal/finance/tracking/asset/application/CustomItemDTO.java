package personal.finance.tracking.asset.application;

import java.math.BigDecimal;
import java.util.UUID;

public class CustomItemDTO extends ItemDTO {

    public CustomItemDTO() {
    }

    public CustomItemDTO(UUID id, BigDecimal money, String name) {
        super(id, money, name);
    }
}
