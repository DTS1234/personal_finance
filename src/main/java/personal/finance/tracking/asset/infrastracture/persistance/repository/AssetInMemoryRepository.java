package personal.finance.tracking.asset.infrastracture.persistance.repository;

import personal.finance.tracking.asset.domain.Asset;
import personal.finance.tracking.asset.domain.AssetId;
import personal.finance.tracking.asset.domain.AssetRepository;

import java.util.HashMap;
import java.util.Map;

public class AssetInMemoryRepository implements AssetRepository {

    private final Map<AssetId, Asset> assets = new HashMap<>();

    @Override
    public Asset findById(AssetId assetId) {
        return assets.get(assetId);
    }

    @Override
    public Asset save(Asset asset) {
        this.assets.put(asset.getId(), asset);
        return asset;
    }

    public AssetInMemoryRepository clear() {
        assets.clear();
        return this;
    }
}
