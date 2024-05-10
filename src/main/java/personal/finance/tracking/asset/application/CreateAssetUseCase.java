package personal.finance.tracking.asset.application;

import lombok.RequiredArgsConstructor;
import personal.finance.common.UseCase;
import personal.finance.tracking.asset.domain.Asset;
import personal.finance.tracking.asset.domain.AssetId;
import personal.finance.tracking.asset.domain.AssetRepository;

@RequiredArgsConstructor
public class CreateAssetUseCase implements UseCase<Asset> {

    private final Asset asset;
    private final AssetRepository assetRepository;

    @Override
    public Asset execute() {
        this.asset.setId(AssetId.random());
        return this.assetRepository.save(this.asset);
    }
}
