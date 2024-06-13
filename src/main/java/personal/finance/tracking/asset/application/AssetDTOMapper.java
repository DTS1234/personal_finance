package personal.finance.tracking.asset.application;

import personal.finance.tracking.asset.domain.Asset;
import personal.finance.tracking.asset.domain.AssetId;
import personal.finance.tracking.asset.domain.Item;
import personal.finance.tracking.asset.infrastracture.persistance.entity.AssetEntity;
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

    public static AssetDTO dto(AssetEntity asset) {
        return new AssetDTO(
            asset.getId(),
            asset.getMoneyValue(),
            itemsDto(asset),
            asset.getName(),
            asset.getType(),
            asset.getSummaryId()
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
            .map(ItemDTOMapper::dto)
            .collect(Collectors.toList());
    }

    private static List<ItemDTO> itemsDto(AssetEntity asset) {
        return asset.getItemEntities()
            .stream()
            .map(ItemDTOMapper::dto)
            .collect(Collectors.toList());
    }

    public static Asset from(AssetDTO assetDTO) {
        return new Asset(
            getOrCreateId(assetDTO),
            new Money(assetDTO.money, Currency.EUR),
            assetDTO.name, mapItems(assetDTO, Currency.EUR),
            assetDTO.type,
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
            .map(ItemDTOMapper::from)
            .collect(Collectors.toList());
    }
}
