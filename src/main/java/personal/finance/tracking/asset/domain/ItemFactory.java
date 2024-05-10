package personal.finance.tracking.asset.domain;

import personal.finance.tracking.summary.domain.Money;

public class ItemFactory {

    public Item createItem(String type, Object object) {
        if (type.equals("STOCK")) {
            StockItemRequest request = (StockItemRequest) object;
            StockItem stockItem = new StockItem();
            stockItem.setId(ItemId.random());
            stockItem.setQuantity(request.quantity());
            stockItem.setPurchasePrice(new Money(request.purchasePrice()));
            stockItem.setCurrentPrice(new Money(request.currentPrice()));
            stockItem.setMoney(new Money(request.currentPrice().multiply(request.quantity())));
            stockItem.setName(request.name());

            return stockItem;
        } else {
            ItemRequest request = (ItemRequest) object;
            Item item = new Item();
            item.setId(ItemId.random());
            item.setName(request.name());
            item.setQuantity(request.quantity());
            item.setMoney(request.money());
            return item;
        }
    }

}
