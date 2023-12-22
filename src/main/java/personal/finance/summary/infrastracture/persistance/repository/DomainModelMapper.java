package personal.finance.summary.infrastracture.persistance.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import personal.finance.summary.domain.UserRepository;
import personal.finance.summary.domain.Asset;
import personal.finance.summary.domain.AssetId;
import personal.finance.summary.domain.Currency;
import personal.finance.summary.domain.Item;
import personal.finance.summary.domain.ItemId;
import personal.finance.summary.domain.Money;
import personal.finance.summary.domain.Summary;
import personal.finance.summary.domain.SummaryId;
import personal.finance.summary.infrastracture.persistance.entity.AssetEntity;
import personal.finance.summary.infrastracture.persistance.entity.ItemEntity;
import personal.finance.summary.infrastracture.persistance.entity.SummaryEntity;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
class DomainModelMapper {

    private final UserRepository userRepository;

    Summary map(SummaryEntity summaryEntity) {
        Currency currency = userRepository.getCurrency(summaryEntity.getUserId());
        Money money = new Money(summaryEntity.getMoneyValue()).convertFromTo(Currency.EUR, currency);
        return new Summary(
            new SummaryId(summaryEntity.getId()),
            summaryEntity.getUserId(),
            money,
            summaryEntity.getDate(),
            summaryEntity.getState(),
            mapAssets(summaryEntity, currency)
        );
    }

    private List<Asset> mapAssets(SummaryEntity summaryEntity, Currency currency) {
        return summaryEntity.getAssetEntities().stream().map(a -> map(a, currency)).collect(Collectors.toList());
    }

    Asset map(AssetEntity assetEntity, Currency currency) {
        Money money = new Money(assetEntity.getMoneyValue()).convertFromTo(Currency.EUR, currency);
        return new Asset(new AssetId(assetEntity.getId()), money,
            assetEntity.getName(), mapItems(assetEntity, currency));
    }

    private List<Item> mapItems(AssetEntity assetEntity, Currency currency) {
        return assetEntity.getItemEntities().stream().map(i -> map(i, currency)).collect(
            Collectors.toList());
    }

    Item map(ItemEntity itemEntity, Currency currency) {
        Money money = new Money(itemEntity.getMoneyValue()).convertFromTo(Currency.EUR, currency);
        return new Item(new ItemId(itemEntity.getId()), money, itemEntity.getName(),
            itemEntity.getQuantity());
    }

    SummaryEntity map(Summary summary) {
        Currency currency = summary.getMoney().getCurrency();
        Money moneyInEuro = summary.getMoney().convertFromTo(currency, Currency.EUR);
        return new SummaryEntity(getIdValue(summary), summary.getUserId(), moneyInEuro.getMoneyValue(),
            summary.getDate(), summary.getState(),
            mapAssets(summary));
    }

    private static Long getIdValue(Summary summary) {
        SummaryId id = summary.getId();
        if (id == null) {
            return null;
        }
        return id.getValue();
    }

    private static Long getIdValue(Asset asset) {
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
        Currency currency = asset.getMoney().getCurrency();
        Money moneyInEuro = asset.getMoney().convertFromTo(currency, Currency.EUR);
        return new AssetEntity(getIdValue(asset), moneyInEuro.getMoneyValue(), asset.getName(), mapItems(asset));
    }

    private List<ItemEntity> mapItems(Asset asset) {
        return asset.getItems().stream().map(this::map).collect(Collectors.toList());
    }

    ItemEntity map(Item item) {
        Currency currency = item.getMoney().getCurrency();
        Money moneyInEuro = item.getMoney().convertFromTo(currency, Currency.EUR);
        return new ItemEntity(getIdValue(item), moneyInEuro.getMoneyValue(), item.getName(),
            item.getQuantity());
    }

    private static Long getIdValue(Item item) {
        ItemId id = item.getId();
        if (id == null) {
            return null;
        }
        return id.getValue();
    }

}
