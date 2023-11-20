package personal.finance.summary.application.dto;

import personal.finance.summary.domain.Asset;
import personal.finance.summary.domain.AssetId;
import personal.finance.summary.domain.Item;
import personal.finance.summary.domain.ItemId;
import personal.finance.summary.domain.Money;
import personal.finance.summary.domain.Summary;
import personal.finance.summary.domain.SummaryId;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class DTOMapper {

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    public static SummaryDTO dto(Summary summary) {
        return new SummaryDTO(
            summary.getId().getValue(),
            summary.getUserId(),
            summary.getMoney().getMoneyValue(),
            summary.getState(),
            dateFormatter.format(summary.getDate()),
            assetsDto(summary)
        );
    }

    private static List<AssetDTO> assetsDto(Summary summary) {
        return summary.getAssets().stream().map(a -> new AssetDTO(
            getId(a),
            a.getMoney().getMoneyValue(),
            itemsDto(a),
            a.getName())).collect(Collectors.toList());
    }

    private static Long getId(Asset a) {
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

    private static Long getId(Item i) {
        if (i.getId() == null) {
            return null;
        }
        return i.getId().getValue();
    }


    public static Summary from(SummaryDTO summaryDTO) {
        return new Summary(
            new SummaryId(summaryDTO.id),
            summaryDTO.userId,
            new Money(summaryDTO.money),
            LocalDateTime.parse(summaryDTO.date, dateFormatter),
            summaryDTO.state,
            mapAssets(summaryDTO)
        );
    }



    private static List<Asset> mapAssets(SummaryDTO summaryDTO) {
        return summaryDTO.assets.stream()
            .map(a -> new Asset(new AssetId(a.id), new Money(a.money), a.name, mapItems(a)))
            .collect(Collectors.toList());
    }

    private static List<Item> mapItems(AssetDTO assetDTO) {
        return assetDTO.items.stream()
            .map(i -> new Item(new ItemId(i.id), new Money(i.money), i.name, i.quantity))
            .collect(Collectors.toList());
    }
}
