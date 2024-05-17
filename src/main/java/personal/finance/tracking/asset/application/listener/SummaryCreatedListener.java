package personal.finance.tracking.asset.application.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import personal.finance.common.events.EventListener;
import personal.finance.common.events.EventPublisher;
import personal.finance.tracking.asset.application.CreateNewSummaryAssetsUseCase;
import personal.finance.tracking.asset.domain.Asset;
import personal.finance.tracking.asset.domain.AssetRepository;
import personal.finance.tracking.asset.domain.events.AssetCreated;
import personal.finance.tracking.summary.domain.SummaryId;
import personal.finance.tracking.summary.domain.events.SummaryCreated;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class SummaryCreatedListener implements EventListener<SummaryCreated> {

    private final AssetRepository assetRepository;
    private final EventPublisher eventPublisher;

    @Override
    @org.springframework.context.event.EventListener
    public void handle(SummaryCreated event) {
        log.info("Handling summary created, creating new assets ...");
        List<Asset> assets = new CreateNewSummaryAssetsUseCase(assetRepository, new SummaryId(event.previousSummaryId),
            new SummaryId(event.newSummaryId)).execute();
        assets.forEach(a -> {
            eventPublisher.publishEvent(new AssetCreated(event.newSummaryId, a, Instant.now(), UUID.randomUUID()));
        });
    }
}
