package personal.finance.tracking.asset.infrastracture.persistance.repository;

import org.springframework.stereotype.Component;
import personal.finance.tracking.asset.domain.Asset;
import personal.finance.tracking.asset.domain.AssetId;
import personal.finance.tracking.asset.domain.Item;
import personal.finance.tracking.asset.domain.ItemId;
import personal.finance.tracking.asset.infrastracture.persistance.entity.AssetEntity;
import personal.finance.tracking.asset.infrastracture.persistance.entity.ItemEntity;
import personal.finance.tracking.summary.domain.Money;
import personal.finance.tracking.summary.domain.SummaryId;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class AssetModelMapper {

    Asset map(AssetEntity assetEntity) {
        return new Asset(new AssetId(assetEntity.getId()), new Money(assetEntity.getMoneyValue()),
            assetEntity.getName(), mapItems(assetEntity), new SummaryId(assetEntity.getSummaryId()));
    }

    private List<Item> mapItems(AssetEntity assetEntity) {
        return assetEntity.getItemEntities().stream().map(this::map).collect(
            Collectors.toList());
    }

    Item map(ItemEntity itemEntity) {
        return new Item(new ItemId(itemEntity.getId()), new Money(itemEntity.getMoneyValue()), itemEntity.getName(),
            itemEntity.getQuantity());
    }

    AssetEntity map(Asset asset) {
        return new AssetEntity(getIdValue(asset), asset.getMoney().getMoneyValue(), asset.getName(), mapItems(asset), asset.getType(), asset.getSummaryId().getValue());
    }

    private List<ItemEntity> mapItems(Asset asset) {
        return asset.getItems().stream().map(this::map).collect(Collectors.toList());
    }

    ItemEntity map(Item item) {
        return new ItemEntity(getIdValue(item), item.getMoney().getMoneyValue(), item.getName(),
            item.getQuantity());
    }

    private static UUID getIdValue(Item item) {
        ItemId id = item.getId();
        if (id == null) {
            return null;
        }
        return id.getValue();
    }

    private static UUID getIdValue(Asset asset) {
        AssetId id = asset.getId();
        if (id == null) {
            return null;
        }
        return id.getValue();
    }
}
