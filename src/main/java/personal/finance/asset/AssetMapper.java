package personal.finance.asset;

import personal.finance.asset.item.ItemMapper;
import personal.finance.summary.domain.model.AssetDomain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

public class AssetMapper {

    public static Asset toEntity(AssetDomain assetDomain) {
        Asset asset = Asset.builder()
                .id(assetDomain.getId())
                .name(assetDomain.getName())
                .moneyValue(Optional.ofNullable(assetDomain.getMoneyValue()).orElse(BigDecimal.ZERO))
                .items(Optional.ofNullable(assetDomain.getItems()).orElse(new ArrayList<>()).stream().map(ItemMapper::toEntity).collect(Collectors.toList()))
                .buildAsset();
        return asset;

    }

    public static AssetDomain toDomain(Asset asset) {
        return AssetDomain.builder()
                .id(asset.getId())
                .name(asset.getName())
                .moneyValue(Optional.ofNullable(asset.getMoneyValue()).orElse(BigDecimal.ZERO))
                .items(Optional.ofNullable(asset.getItems()).orElse(new ArrayList<>()).stream().map(ItemMapper::toDomain).collect(Collectors.toList()))
                .build();
    }
}
