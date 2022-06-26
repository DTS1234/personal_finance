package personal.finance.asset;

import personal.finance.asset.item.ItemMapper;
import personal.finance.summary.domain.model.AssetDomain;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

public class AssetMapper {

    public static Asset toEntity(AssetDomain assetDomain) {
        return Asset.builder()
                .id(assetDomain.getId())
                .name(assetDomain.getName())
                .moneyValue(assetDomain.getMoneyValue())
                .items(Optional.ofNullable(assetDomain.getItems()).orElse(new ArrayList<>()).stream().map(ItemMapper::toEntity).collect(Collectors.toList()))
                .buildAsset();
    }

}
