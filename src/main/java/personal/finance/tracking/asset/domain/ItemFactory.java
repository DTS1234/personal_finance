package personal.finance.tracking.asset.domain;

import personal.finance.tracking.asset.application.CustomItemDTO;
import personal.finance.tracking.asset.application.StockItemDTO;
import personal.finance.tracking.summary.domain.Money;

public class ItemFactory {

    public Item createItem(Object object) {
        if (object instanceof StockItemDTO request) {
            StockItem stockItem = new StockItem();
            stockItem.setId(ItemId.random());
            stockItem.setQuantity(request.getQuantity());
            stockItem.setPurchasePrice(new Money(request.getPurchasePrice()));
            stockItem.setCurrentPrice(new Money(request.getCurrentPrice()));
            stockItem.setMoney(new Money(stockItem.getCurrentPrice().getMoneyValue().multiply(request.getQuantity())));
            stockItem.setName(request.getName());
            stockItem.setTicker(request.getTicker());
            return stockItem;
        } else {
            CustomItemDTO request = (CustomItemDTO) object;
            return new CustomItem(ItemId.random(), new Money(request.getMoney()), request.getName());
        }
    }

}
