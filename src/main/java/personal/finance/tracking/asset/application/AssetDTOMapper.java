package personal.finance.tracking.asset.application;

import personal.finance.tracking.asset.domain.Asset;
import personal.finance.tracking.asset.domain.AssetId;
import personal.finance.tracking.asset.domain.AssetType;
import personal.finance.tracking.asset.domain.Item;
import personal.finance.tracking.asset.domain.ItemId;
import personal.finance.tracking.asset.infrastracture.persistance.entity.AssetEntity;
import personal.finance.tracking.summary.application.dto.AssetDTO;
import personal.finance.tracking.summary.application.dto.ItemDTO;
import personal.finance.tracking.summary.application.dto.SummaryDTO;
import personal.finance.tracking.summary.domain.Currency;
import personal.finance.tracking.summary.domain.Money;
import personal.finance.tracking.summary.domain.SummaryId;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class AssetDTOMapper {

    public static AssetDTO dto(Asset asset) {
        return new AssetDTO(
            getId(asset),
            asset.getMoney().getMoneyValue(),
            itemsDto(asset),
            asset.getName(),
            asset.getType(),
            asset.getSummaryIdValue()
        );
    }

    private static UUID getId(Asset a) {
        AssetId id = a.getId();
        if (id == null) {
            return null;
        }
        return id.getValue();
    }

    private static List<ItemDTO> itemsDto(Asset asset) {
        return asset.getItems()
            .stream()
            .map(i -> new ItemDTO(getId(i), i.getMoney().getMoneyValue(), i.getName(), i.getQuantity()))
            .collect(Collectors.toList());
    }

    private static List<ItemDTO> itemsDto(AssetEntity asset) {
        return asset.getItemEntities()
            .stream()
            .map(i -> new ItemDTO(i.getId(), i.getMoneyValue(), i.getName(), i.getQuantity()))
            .collect(Collectors.toList());
    }

    private static UUID getId(Item i) {
        if (i.getId() == null) {
            return UUID.randomUUID();
        }
        return i.getId().getValue();
    }

    public static Asset from(AssetDTO assetDTO) {
        return new Asset(
            getOrCreateId(assetDTO),
            new Money(assetDTO.money, Currency.EUR),
            assetDTO.name, mapItems(assetDTO, Currency.EUR),
            AssetType.NORMAL,
            new SummaryId(assetDTO.summaryId));
    }

    private static AssetId getOrCreateId(AssetDTO a) {
        if (a.id == null) {
            return AssetId.random();
        }
        return new AssetId(a.id);
    }

    private static List<Item> mapItems(AssetDTO assetDTO, Currency currency) {
        return assetDTO.items.stream()
            .map(i -> new Item(getOrCreateId(i), new Money(i.money, currency), i.name, i.quantity))
            .collect(Collectors.toList());
    }

    private static ItemId getOrCreateId(ItemDTO i) {
        if (i.id == null) {
            return ItemId.random();
        }
        return new ItemId(i.id);
    }

}
