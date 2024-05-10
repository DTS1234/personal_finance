package personal.finance.tracking.asset.application;

import lombok.RequiredArgsConstructor;
import personal.finance.common.UseCase;
import personal.finance.tracking.asset.domain.Asset;
import personal.finance.tracking.asset.domain.AssetId;
import personal.finance.tracking.asset.domain.AssetRepository;

import java.util.UUID;

@RequiredArgsConstructor
public class UpdateAssetUseCase implements UseCase<Asset> {

    private final AssetRepository assetRepository;
    private final UUID assetId;
    private final Asset newAsset;

    @Override
    public Asset execute() {
        Asset assetFound = assetRepository.findById(new AssetId(assetId));

        if (assetFound == null) {
            throw new IllegalStateException("Cannot find an asset to update with this id: " + assetId);
        }

        return assetRepository.save(newAsset);
    }
}
