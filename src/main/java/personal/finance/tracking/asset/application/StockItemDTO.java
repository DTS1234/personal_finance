package personal.finance.tracking.asset.application;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
public class StockItemDTO extends ItemDTO {
     String ticker;
     BigDecimal purchasePrice;
     BigDecimal currentPrice;
     BigDecimal quantity;

     public StockItemDTO() {
     }

     public StockItemDTO(UUID id, BigDecimal money, String name, String ticker, BigDecimal purchasePrice,
         BigDecimal currentPrice, BigDecimal quantity) {
          super(id, money, name);
          this.ticker = ticker;
          this.purchasePrice = purchasePrice;
          this.currentPrice = currentPrice;
          this.quantity = quantity;
     }
}
