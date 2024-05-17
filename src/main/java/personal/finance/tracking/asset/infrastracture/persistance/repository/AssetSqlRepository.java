package personal.finance.tracking.asset.infrastracture.persistance.repository;

import lombok.RequiredArgsConstructor;
import personal.finance.tracking.asset.domain.Asset;
import personal.finance.tracking.asset.domain.AssetId;
import personal.finance.tracking.asset.domain.AssetRepository;
import personal.finance.tracking.asset.infrastracture.persistance.entity.AssetEntity;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class AssetSqlRepository implements AssetRepository {

    private final AssetModelMapper mapper;
    private final AssetJpaRepository assetJpaRepository;

    @Override
    public Asset findById(AssetId assetId) {
        return assetJpaRepository.findById(assetId.getValue()).map(mapper::map).orElse(null);
    }

    @Override
    public Asset save(Asset asset) {
        AssetEntity assetEntity = mapper.map(asset);
        return mapper.map(assetJpaRepository.save(assetEntity));
    }

    @Override
    public List<Asset> saveAll(List<Asset> assetList) {
        return assetJpaRepository.saveAll(assetList.stream().map(mapper::map).toList())
            .stream().map(mapper::map).toList();
    }

    @Override
    public List<Asset> findAllBySummaryId(UUID summaryId) {
        return assetJpaRepository.findAllBySummaryId(summaryId).stream().map(mapper::map).toList();
    }
}
