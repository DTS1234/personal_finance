package personal.finance.tracking.asset.application;

import personal.finance.tracking.asset.domain.CustomItem;
import personal.finance.tracking.asset.domain.Item;
import personal.finance.tracking.asset.domain.ItemFactory;
import personal.finance.tracking.asset.domain.ItemId;
import personal.finance.tracking.asset.domain.StockItem;
import personal.finance.tracking.asset.infrastracture.persistance.entity.ItemEntity;
import personal.finance.tracking.asset.infrastracture.persistance.entity.StockItemEntity;

public class ItemDTOMapper {

    public static Item from(ItemDTO itemDTO) {
        if (itemDTO instanceof StockItemDTO stockItemDto) {
            return new ItemFactory().createItem(itemDTO);
        }
        return new ItemFactory().createItem(itemDTO);
    }

    private static ItemId getId(ItemDTO itemDTO) {
        if (itemDTO.id == null) {
            return ItemId.random();
        }
        return new ItemId(itemDTO.id);
    }

    public static ItemDTO dto(Item item) {
        return switch (item) {
            case StockItem stockItem -> mapStockItem(stockItem);
            case CustomItem customItem -> mapCustomItem(customItem);
            default -> throw new IllegalStateException("No item type matched for given item.");
        };
    }

    private static ItemDTO mapCustomItem(CustomItem customItem) {
        return new CustomItemDTO(customItem.getIdValue(), customItem.getMoney().getMoneyValue(), customItem.getName());
    }

    private static StockItemDTO mapStockItem(StockItem stockItem) {
        return new StockItemDTO(stockItem.getIdValue(), stockItem.getMoney().getMoneyValue(),
            stockItem.getName(), stockItem.getTicker(),
            stockItem.getPurchasePrice().getMoneyValue(), stockItem.getCurrentPrice().getMoneyValue(),
            stockItem.getQuantity());
    }

    public static ItemDTO dto(ItemEntity itemEntity) {
        if (itemEntity instanceof StockItemEntity stockItem) {
            return new StockItemDTO(stockItem.getId(), stockItem.getMoneyValue(),
                stockItem.getName(), stockItem.getTicker(),
                stockItem.getPurchasePrice(), stockItem.getCurrentPrice(),
                stockItem.getQuantity());
        }
        return new CustomItemDTO(itemEntity.getId(), itemEntity.getMoneyValue(), itemEntity.getName());
    }
}


