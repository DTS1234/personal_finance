package personal.finance.tracking.asset.domain;

import java.util.UUID;

public interface AssetRepository {

    Asset findById(AssetId assetId);

    Asset save(Asset asset);
}
