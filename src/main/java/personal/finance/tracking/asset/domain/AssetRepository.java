package personal.finance.tracking.asset.domain;

import java.util.List;
import java.util.UUID;

public interface AssetRepository {

    Asset findById(AssetId assetId);

    Asset save(Asset asset);

    List<Asset> saveAll(List<Asset> assets);

    List<Asset> findAllBySummaryId(UUID summaryId);
}
