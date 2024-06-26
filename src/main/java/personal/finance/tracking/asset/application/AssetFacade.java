package personal.finance.tracking.asset.application;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import personal.finance.common.events.EventPublisher;
import personal.finance.tracking.asset.domain.Asset;
import personal.finance.tracking.asset.domain.AssetId;
import personal.finance.tracking.asset.domain.AssetRepository;
import personal.finance.tracking.asset.domain.events.AssetCreated;
import personal.finance.tracking.asset.domain.events.AssetUpdated;

import java.time.Instant;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class AssetFacade {

    private final AssetRepository assetRepository;
    @Getter
    private final EventPublisher eventPublisher;

    public Asset updateAsset(UUID userId, UUID summaryId, UUID assetId, Asset updatedAsset) {
        Asset oldAsset = assetRepository.findById(new AssetId(assetId));
        eventPublisher.publishEvent(new AssetUpdated(summaryId, updatedAsset, oldAsset, Instant.now(), UUID.randomUUID()));
        return new UpdateAssetUseCase(assetRepository, assetId, updatedAsset).execute();
    }

    public Asset createAsset(UUID summaryId, Asset asset) {
        Asset assetCreated = new CreateAssetUseCase(asset, assetRepository).execute();
        eventPublisher.publishEvent(new AssetCreated(summaryId, assetCreated, Instant.now(), UUID.randomUUID()));
        return assetCreated;
    }
}
