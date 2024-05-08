package personal.finance.summary.infrastracture.persistance.repository;

import lombok.RequiredArgsConstructor;
import personal.finance.summary.domain.Asset;
import personal.finance.summary.domain.AssetId;
import personal.finance.summary.domain.Item;
import personal.finance.summary.domain.ItemId;
import personal.finance.summary.domain.Money;
import personal.finance.summary.domain.Summary;
import personal.finance.summary.domain.SummaryId;
import personal.finance.summary.infrastracture.persistance.entity.AssetEntity;
import personal.finance.summary.infrastracture.persistance.entity.ItemEntity;
import personal.finance.summary.infrastracture.persistance.entity.SummaryEntity;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
class DomainModelMapper {

    Summary map(SummaryEntity summaryEntity) {
        return new Summary(
            new SummaryId(summaryEntity.getId()),
            summaryEntity.getUserId(),
            new Money(summaryEntity.getMoneyValue()),
            summaryEntity.getDate(),
            summaryEntity.getState(),
            mapAssets(summaryEntity)
        );
    }

    private List<Asset> mapAssets(SummaryEntity summaryEntity) {
        return summaryEntity.getAssetEntities().stream().map(this::map).collect(Collectors.toList());
    }

    Asset map(AssetEntity assetEntity) {
        return new Asset(new AssetId(assetEntity.getId()), new Money(assetEntity.getMoneyValue()),
            assetEntity.getName(), mapItems(assetEntity));
    }

    private List<Item> mapItems(AssetEntity assetEntity) {
        return assetEntity.getItemEntities().stream().map(this::map).collect(
            Collectors.toList());
    }

    Item map(ItemEntity itemEntity) {
        return new Item(new ItemId(itemEntity.getId()), new Money(itemEntity.getMoneyValue()), itemEntity.getName(),
            itemEntity.getQuantity());
    }

    SummaryEntity map(Summary summary) {
        return new SummaryEntity(getIdValue(summary), summary.getUserId(), summary.getMoney().getMoneyValue(),
            summary.getDate(), summary.getState(),
            mapAssets(summary));
    }

    private static UUID getIdValue(Summary summary) {
        SummaryId id = summary.getId();
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

    private List<AssetEntity> mapAssets(Summary summary) {
        return summary.getAssets().stream().map(this::map).collect(
            Collectors.toList());
    }

    AssetEntity map(Asset asset) {
        return new AssetEntity(getIdValue(asset), asset.getMoney().getMoneyValue(), asset.getName(), mapItems(asset), asset.getType());
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
}
