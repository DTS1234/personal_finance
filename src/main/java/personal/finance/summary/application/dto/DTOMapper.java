package personal.finance.summary.application.dto;

import personal.finance.summary.domain.Asset;
import personal.finance.summary.domain.AssetId;
import personal.finance.summary.domain.AssetType;
import personal.finance.summary.domain.Currency;
import personal.finance.summary.domain.Item;
import personal.finance.summary.domain.ItemId;
import personal.finance.summary.domain.Money;
import personal.finance.summary.domain.Summary;
import personal.finance.summary.domain.SummaryId;
import personal.finance.summary.infrastracture.persistance.entity.AssetEntity;
import personal.finance.summary.infrastracture.persistance.entity.SummaryEntity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class DTOMapper {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    public static SummaryDTO dto(Summary summary) {
        return new SummaryDTO(
            summary.getId().getValue(),
            summary.getUserId(),
            summary.getMoney().getMoneyValue(),
            summary.getMoney().getCurrency(),
            summary.getState(),
            DATE_FORMATTER.format(summary.getDate()),
            assetsDto(summary)
        );
    }

    public static SummaryDTO dto(SummaryEntity summary) {
        return new SummaryDTO(
            summary.getId(),
            summary.getUserId(),
            summary.getMoneyValue(),
            Currency.EUR,
            summary.getState(),
            DATE_FORMATTER.format(summary.getDate()),
            assetsDto(summary)
        );
    }

    private static List<AssetDTO> assetsDto(Summary summary) {
        return summary.getAssets().stream().map(a -> new AssetDTO(
            getId(a),
            a.getMoney().getMoneyValue(),
            itemsDto(a),
            a.getName(),
            a.getType())).collect(Collectors.toList());
    }

    private static List<AssetDTO> assetsDto(SummaryEntity summary) {
        return summary.getAssetEntities().stream().map(a -> new AssetDTO(
            a.getId(),
            a.getMoneyValue(),
            itemsDto(a),
            a.getName(),
            a.getType())).collect(Collectors.toList());
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


    public static Summary from(SummaryDTO summaryDTO) {
        return new Summary(
            new SummaryId(summaryDTO.id),
            summaryDTO.userId,
            new Money(summaryDTO.money, summaryDTO.currency),
            LocalDateTime.parse(summaryDTO.date, DATE_FORMATTER),
            summaryDTO.state,
            mapAssets(summaryDTO)
        );
    }

    private static List<Asset> mapAssets(SummaryDTO summaryDTO) {
        return summaryDTO.assets.stream()
            .map(a -> new Asset(
                getOrCreateId(a), new Money(a.money, summaryDTO.currency), a.name, mapItems(a, summaryDTO.currency), AssetType.NORMAL))
            .collect(Collectors.toList());
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
