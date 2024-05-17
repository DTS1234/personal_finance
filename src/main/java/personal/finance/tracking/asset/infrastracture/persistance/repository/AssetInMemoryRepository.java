package personal.finance.tracking.asset.infrastracture.persistance.repository;

import personal.finance.tracking.asset.domain.Asset;
import personal.finance.tracking.asset.domain.AssetId;
import personal.finance.tracking.asset.domain.AssetRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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

    @Override
    public List<Asset> saveAll(List<Asset> assets) {
        assets.forEach(a -> this.assets.put(a.getId(), a));
        return assets;
    }

    @Override
    public List<Asset> findAllBySummaryId(UUID summaryId) {
        return assets.values().stream().filter(a -> a.getSummaryId().getValue().equals(summaryId)).toList();
    }

    public AssetInMemoryRepository clear() {
        assets.clear();
        return this;
    }
}
