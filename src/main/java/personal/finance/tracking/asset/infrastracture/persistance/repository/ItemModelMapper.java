package personal.finance.tracking.asset.infrastracture.persistance.repository;

import personal.finance.tracking.asset.domain.CustomItem;
import personal.finance.tracking.asset.domain.Item;
import personal.finance.tracking.asset.domain.ItemId;
import personal.finance.tracking.asset.domain.StockItem;
import personal.finance.tracking.asset.infrastracture.persistance.entity.CustomItemEntity;
import personal.finance.tracking.asset.infrastracture.persistance.entity.ItemEntity;
import personal.finance.tracking.asset.infrastracture.persistance.entity.StockItemEntity;
import personal.finance.tracking.summary.domain.Money;

import java.util.UUID;

public class ItemModelMapper {

    ItemEntity map(Item item) {
        if (item instanceof StockItem) {
            return StockItemEntity.builder()
                .name(item.getName())
                .id(getIdValue(item))
                .moneyValue(item.getMoney().getMoneyValue())
                .purchasePrice(((StockItem) item).getPurchasePrice().getMoneyValue())
                .currentPrice(((StockItem) item).getCurrentPrice().getMoneyValue())
                .ticker(((StockItem) item).getTicker())
                .quantity(((StockItem) item).getQuantity())
                .build();
        }
        return CustomItemEntity.builder()
            .moneyValue(item.getMoney().getMoneyValue())
            .name(item.getName())
            .id(item.getIdValue())
            .build();
    }

    private static UUID getIdValue(Item item) {
        ItemId id = item.getId();
        if (id == null) {
            return null;
        }
        return id.getValue();
    }

    Item map(ItemEntity itemEntity) {
        if (itemEntity instanceof StockItemEntity stockItem) {
            return StockItem.builder()
                .id(new ItemId(stockItem.getId()))
                .purchasePrice(new Money(stockItem.getPurchasePrice()))
                .currentPrice(new Money(stockItem.getCurrentPrice()))
                .money(new Money(stockItem.getMoneyValue()))
                .ticker(stockItem.getTicker())
                .name(stockItem.getName())
                .quantity(stockItem.getQuantity())
                .build();
        }
        return new CustomItem(new ItemId(itemEntity.getId()), new Money(itemEntity.getMoneyValue()), itemEntity.getName());
    }


}
